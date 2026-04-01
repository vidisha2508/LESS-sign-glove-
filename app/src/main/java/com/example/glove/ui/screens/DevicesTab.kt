package com.example.glove.ui.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.glove.bluetooth.BluetoothConnectionState
import com.example.glove.ui.GloveViewModel
import com.example.glove.ui.components.GlassCard
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DevicesTab(viewModel: GloveViewModel) {
    val connectionState by viewModel.bluetoothManager.connectionState.collectAsState()
    val scannedDevices by viewModel.bluetoothManager.scannedDevices.collectAsState()

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
    } else {
        listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    val permissionsState = rememberMultiplePermissionsState(permissions)

    // Automatically scan logic handles inside ViewModel typically, or trigger scan button below

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(Modifier.height(16.dp))
        Text("Paired Devices", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(16.dp))
        
        if (!permissionsState.allPermissionsGranted) {
            Button(
                onClick = { permissionsState.launchMultiplePermissionRequest() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Grant Bluetooth Permissions", color = MaterialTheme.colorScheme.onSecondary)
            }
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Status: ${connectionState.name}",
                    color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = { viewModel.startScan() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Refresh", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if(connectionState == BluetoothConnectionState.CONNECTING) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
            }

            LazyColumn(Modifier.fillMaxSize()) {
                items(scannedDevices) { device ->
                    
                    GlassCard(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.connectToDevice(device.macAddress) }) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(device.name ?: "Unknown HC-05", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(device.macAddress, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), style = MaterialTheme.typography.bodySmall)
                            }
                            
                            if (connectionState == BluetoothConnectionState.CONNECTED) {
                                Text("Select to Re-Pair", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.labelSmall)
                            } else {
                                Text("Connect", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
