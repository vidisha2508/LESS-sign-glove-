package com.example.glove.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.glove.bluetooth.BluetoothConnectionState
import com.example.glove.parsing.GestureParser
import com.example.glove.ui.GloveViewModel
import com.example.glove.ui.components.GlassCard

@Composable
fun StatusTab(viewModel: GloveViewModel) {
    val connectionState by viewModel.bluetoothManager.connectionState.collectAsState()
    val lastGesture by viewModel.lastReceivedGesture.collectAsState()
    val lastSpoken by viewModel.lastSpokenWord.collectAsState()
    val currentLayout by viewModel.selectedLayout.collectAsState()

    val isConnected = connectionState == BluetoothConnectionState.CONNECTED

    // Glowing animation mechanism
    val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
    val glowRadius by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = if (isConnected) 40f else 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Glow"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOP SECTION
        Text(
            text = "Project GLOVE", 
            style = MaterialTheme.typography.displaySmall, 
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = currentLayout?.name ?: "No Profile Selected", 
            style = MaterialTheme.typography.bodyLarge, 
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.weight(1f))

        // CENTER SECTION: Circular Bluetooth Target
        val btnColor = if (isConnected) MaterialTheme.colorScheme.primary else Color.DarkGray

        Box(
            modifier = Modifier
                .size(160.dp)
                .shadow(elevation = glowRadius.dp, shape = CircleShape, ambientColor = btnColor, spotColor = btnColor)
                .clip(CircleShape)
                .background(btnColor.copy(alpha = 0.15f))
                .border(2.dp, btnColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Info, contentDescription = "Status", modifier = Modifier.size(72.dp), tint = btnColor)
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = if (isConnected) "SYSTEM ONLINE" else switchTextState(connectionState),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = if (isConnected) "Listening for gesture streams..." else "Explore Devices via footer to connect",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.weight(1f))

        // BOTTOM GLASS INFO CARDS
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Sensor Code", color = MaterialTheme.colorScheme.onBackground.copy(0.7f), style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = lastGesture?.let { GestureParser.intToBinaryString(it) } ?: "---",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Target Word", color = MaterialTheme.colorScheme.onBackground.copy(0.7f), style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    AnimatedContent(targetState = lastSpoken, label = "WordAnim") { spoken ->
                        Text(
                            text = spoken.ifBlank { "Ready" },
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun switchTextState(state: BluetoothConnectionState): String {
    return when(state) {
        BluetoothConnectionState.CONNECTING -> "CONNECTING..."
        BluetoothConnectionState.ERROR -> "SYSTEM ERROR"
        else -> "SYSTEM OFFLINE"
    }
}
