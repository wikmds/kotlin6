package com.example.ari67.presentation

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BleScreen() {
    val context = LocalContext.current
    val viewModel = remember { BleViewModel(context) }

    val devices by viewModel.devices.collectAsState()
    val heartRate by viewModel.heartRate.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()

    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Heart Rate Monitor") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (permissionState.allPermissionsGranted) {
                        if (isScanning) viewModel.stopScan() else viewModel.startScan()
                    } else {
                        permissionState.launchMultiplePermissionRequest()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isScanning) "Stop Scan" else "Start Scan")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ТЕКСТ ПУЛЬСА
            Text(
                text = "Heart Rate: ${heartRate ?: "—"}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text("Status: $connectionState")

            Spacer(modifier = Modifier.height(20.dp))

            if (connectionState == "Connected") {
                Button(onClick = { viewModel.disconnect() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Disconnect")
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(devices) { device ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            .clickable { viewModel.connect(device) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(device.name ?: "Unknown Device", style = MaterialTheme.typography.titleMedium)
                            Text(device.address, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}