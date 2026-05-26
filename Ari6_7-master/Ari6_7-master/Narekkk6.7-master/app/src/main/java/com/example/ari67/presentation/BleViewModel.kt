package com.example.ari67.presentation

import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ari67.data.BleRepository
import kotlinx.coroutines.flow.StateFlow

class BleViewModel(context: Context) : ViewModel() {
    private val repository = BleRepository(context)

    val devices: StateFlow<List<BluetoothDevice>> = repository.devices
    val heartRate: StateFlow<String?> = repository.heartRate
    val connectionState: StateFlow<String> = repository.connectionState
    val isScanning: StateFlow<Boolean> = repository.isScanning

    fun startScan() = repository.startScan()
    fun stopScan() = repository.stopScan()
    fun connect(device: BluetoothDevice) = repository.connect(device)
    fun disconnect() = repository.disconnect()

    override fun onCleared() {
        repository.disconnect()
        super.onCleared()
    }
}