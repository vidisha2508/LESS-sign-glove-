package com.example.glove.ui.screens

import android.Manifest
import android.os.Build
import androidx.compose.animation.animateColorAsState
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
fun DeviceScreen(
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
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Header
        Spacer(modifier = Modifier.height(16.dp))
        Text("Bluetooth Devices", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(24.dp))

        if (!permissionsState.allPermissionsGranted) {
            Button(
                onClick = { permissionsState.launchMultiplePermissionRequest() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Grant Bluetooth Permissions", color = MaterialTheme.colorScheme.onSecondary)
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Status: ${connectionState.name}", color = MaterialTheme.colorScheme.onBackground.copy(0.7f))
                Button(
                    onClick = { viewModel.startScan() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Scan Nearby", color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (connectionState == BluetoothConnectionState.CONNECTING) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            }

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(scannedDevices) { device ->
                    
                    GlassCard(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { viewModel.connectToDevice(device.macAddress) }) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(device.name ?: "Unknown HC-05", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(device.macAddress, color = MaterialTheme.colorScheme.onSurface.copy(0.7f), style = MaterialTheme.typography.bodySmall)
                            }
                            // Visual cue to tap and pair
                            Text("Tap to Pair...", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}
