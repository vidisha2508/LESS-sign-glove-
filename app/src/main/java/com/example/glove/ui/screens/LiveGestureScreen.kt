package com.example.glove.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.glove.parsing.GestureParser
import com.example.glove.ui.GloveViewModel
import com.example.glove.ui.components.GlassCard

@Composable
fun LiveGestureScreen(
    viewModel: GloveViewModel,
    onNavigateToMappings: () -> Unit,
    onNavigateToLayouts: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onBack: () -> Unit
) {
    val lastGesture by viewModel.lastReceivedGesture.collectAsState()
    val lastSpoken by viewModel.lastSpokenWord.collectAsState()
    val currentLayout by viewModel.selectedLayout.collectAsState()
    val allLayouts by viewModel.allLayouts.collectAsState()

    var layoutDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("Disconnect", color = Color(0xFFE57373)) }
            Text("Live Sensor", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
            IconButton(onClick = onNavigateToSettings) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onBackground)
            }
        }
        
        Spacer(Modifier.height(16.dp))

        // Quick Layout Switcher UI Component
        Box {
            Row(
                modifier = Modifier.clickable { layoutDropdownExpanded = true }.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile: ${currentLayout?.name ?: "None"}", 
                    color = MaterialTheme.colorScheme.secondary, 
                    style = MaterialTheme.typography.labelLarge
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Switch Profile", tint = MaterialTheme.colorScheme.secondary)
            }
            DropdownMenu(
                expanded = layoutDropdownExpanded,
                onDismissRequest = { layoutDropdownExpanded = false }
            ) {
                allLayouts.forEach { layout ->
                    DropdownMenuItem(
                        text = { Text(layout.name) },
                        onClick = {
                            viewModel.switchLayout(layout.id)
                            layoutDropdownExpanded = false
                        }
                    )
                }
                Divider()
                DropdownMenuItem(
                    text = { Text("Manage Profiles...") },
                    onClick = {
                        layoutDropdownExpanded = false
                        onNavigateToLayouts()
                    }
                )
            }
        }

        Spacer(Modifier.height(48.dp))

        GlassCard(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = lastGesture?.let { GestureParser.intToBinaryString(it) } ?: "waiting",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(16.dp))
                AnimatedContent(targetState = lastSpoken, label = "Speaker Transition") { spoken ->
                    Text(
                        text = spoken.ifBlank { "Ready to map code" },
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = onNavigateToMappings, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)) { 
                Text("Edit Maps", color = MaterialTheme.colorScheme.onSurface) 
            }
        }
    }
}
