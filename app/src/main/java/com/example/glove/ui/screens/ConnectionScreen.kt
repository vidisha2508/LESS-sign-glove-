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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.glove.bluetooth.BluetoothConnectionState
import com.example.glove.ui.GloveViewModel
import com.example.glove.ui.components.GlassCard
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ConnectionScreen(
    viewModel: GloveViewModel,
    onNavigateToLive: () -> Unit
) {
    val connectionState by viewModel.bluetoothManager.connectionState.collectAsState()
    val scannedDevices by viewModel.bluetoothManager.scannedDevices.collectAsState()

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
    } else {
        listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val permissionsState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(connectionState) {
        if (connectionState == BluetoothConnectionState.CONNECTED) {
            onNavigateToLive()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("GLOVE Connect", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(32.dp))

        if (!permissionsState.allPermissionsGranted) {
            Button(
                onClick = { permissionsState.launchMultiplePermissionRequest() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4DD0E1))
            ) {
                Text("Grant Bluetooth Permissions", color = Color.Black)
            }
        } else {
            Button(
                onClick = { viewModel.startScan() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
            ) {
                Text("Scan Paired Devices", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (connectionState) {
                BluetoothConnectionState.CONNECTING -> CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                BluetoothConnectionState.ERROR -> Text("Connection Failed. Please try pairing again.", color = Color(0xFFE57373))
                else -> Spacer(Modifier.height(8.dp))
            }

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(scannedDevices) { device ->
                    GlassCard(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { viewModel.connectToDevice(device.macAddress) }) {
                        Column {
                            Text(device.name ?: "Unknown Device", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text(device.macAddress, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
