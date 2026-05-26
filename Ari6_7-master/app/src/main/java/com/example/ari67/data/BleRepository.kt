package com.example.ari67.data

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

@SuppressLint("MissingPermission")
class BleRepository(private val context: Context) {

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val adapter = bluetoothManager.adapter

    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devices: StateFlow<List<BluetoothDevice>> = _devices

    private val _heartRate = MutableStateFlow<String?>(null) // Изменили с давления на пульс
    val heartRate: StateFlow<String?> = _heartRate

    private val _connectionState = MutableStateFlow("Disconnected")
    val connectionState: StateFlow<String> = _connectionState

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private var currentGatt: BluetoothGatt? = null

    // Настройки сканера
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val current = _devices.value.toMutableList()
            if (current.none { it.address == device.address }) {
                current.add(device)
                _devices.value = current
            }
        }
    }

    fun startScan() {
        // Проверяем, есть ли адаптер и включен ли он
        if (adapter == null) {
            println("ОШИБКА: Bluetooth адаптер не найден")
            return
        }
        if (!adapter.isEnabled) {
            println("ОШИБКА: Bluetooth выключен на телефоне")
            return
        }

        val scanner = adapter.bluetoothLeScanner
        if (scanner == null) {
            println("ОШИБКА: Не удалось получить LeScanner")
            return
        }

        _devices.value = emptyList()
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        try {
            scanner.startScan(null, settings, scanCallback)
            _isScanning.value = true
            println("Сканирование запущено успешно")
        } catch (e: Exception) {
            println("ОШИБКА при запуске сканирования: ${e.message}")
        }
    }

    fun stopScan() {
        adapter.bluetoothLeScanner?.stopScan(scanCallback)
        _isScanning.value = false
    }

    fun connect(device: BluetoothDevice) {
        stopScan()
        currentGatt = device.connectGatt(context, false, gattCallback)
        _connectionState.value = "Connecting"
    }

    fun disconnect() {
        currentGatt?.disconnect()
        currentGatt?.close()
        currentGatt = null
        _connectionState.value = "Disconnected"
        _heartRate.value = null
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                _connectionState.value = "Connected"
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                _connectionState.value = "Disconnected"
                _heartRate.value = null
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt.getService(HEART_RATE_SERVICE_UUID)
                val characteristic = service?.getCharacteristic(HEART_RATE_MEASUREMENT_UUID)

                characteristic?.let {
                    // Включаем уведомления (Notify)
                    gatt.setCharacteristicNotification(it, true)
                    val descriptor = it.getDescriptor(CCC_DESCRIPTOR_UUID)
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    gatt.writeDescriptor(descriptor)
                }
            }
        }

        // Вызывается, когда датчик присылает новый пульс
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            if (characteristic.uuid == HEART_RATE_MEASUREMENT_UUID) {
                parseHeartRate(characteristic)
            }
        }

        private fun parseHeartRate(characteristic: BluetoothGattCharacteristic) {
            val data = characteristic.value
            if (data == null || data.isEmpty()) return

            val flag = data[0].toInt()
            val format = if (flag and 0x01 == 0) BluetoothGattCharacteristic.FORMAT_UINT8
            else BluetoothGattCharacteristic.FORMAT_UINT16

            val pulse = characteristic.getIntValue(format, 1)
            _heartRate.value = "$pulse bpm"
        }
    }

    companion object {
        // СТАНДАРТНЫЕ UUID ДЛЯ ПУЛЬСА
        private val HEART_RATE_SERVICE_UUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")
        private val HEART_RATE_MEASUREMENT_UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
        private val CCC_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }
}