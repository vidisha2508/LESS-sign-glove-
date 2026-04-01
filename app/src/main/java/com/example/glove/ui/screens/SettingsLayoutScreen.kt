package com.example.glove.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.glove.parsing.GestureParser
import com.example.glove.ui.GloveViewModel
import com.example.glove.ui.components.GlassCard

@Composable
fun SettingsLayoutScreen(
    viewModel: GloveViewModel,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Mappings", "Profiles", "System")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("Back", color = MaterialTheme.colorScheme.onBackground) }
            Spacer(Modifier.width(16.dp))
            Text("Settings Overview", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleLarge)
        }
        
        Spacer(Modifier.height(16.dp))

        TabRow(selectedTabIndex = selectedTab, containerColor = androidx.compose.ui.graphics.Color.Transparent, contentColor = MaterialTheme.colorScheme.primary) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, color = MaterialTheme.colorScheme.onBackground) }
                )
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        when (selectedTab) {
            0 -> MappingEditorView(viewModel)
            1 -> LayoutManagerView(viewModel)
            2 -> SystemTTSView(viewModel)
        }
    }
}

@Composable
fun MappingEditorView(viewModel: GloveViewModel) {
    val mappings by viewModel.currentMappings.collectAsState()
    val currentLayout by viewModel.selectedLayout.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Text("Profile: ${currentLayout?.name ?: "None"}", color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(8.dp))
        LazyColumn(Modifier.fillMaxSize()) {
            items(mappings) { mapping ->
                var textValue by remember(mapping.mappedWord) { mutableStateOf(mapping.mappedWord) }
                GlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = GestureParser.intToBinaryString(mapping.gestureIndex),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(0.3f),
                            style = MaterialTheme.typography.titleMedium
                        )
                        OutlinedTextField(
                            value = textValue,
                            onValueChange = { 
                                textValue = it
                                viewModel.updateMapping(mapping, it)
                            },
                            modifier = Modifier.weight(0.7f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(0.4f)
                            ),
                            singleLine = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LayoutManagerView(viewModel: GloveViewModel) {
    val layouts by viewModel.allLayouts.collectAsState()
    var newLayoutName by remember { mutableStateOf("") }
    
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = newLayoutName,
                onValueChange = { newLayoutName = it },
                label = { Text("New Profile Name", color = MaterialTheme.colorScheme.onSurface.copy(0.7f)) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { 
                    if (newLayoutName.isNotBlank()) {
                        viewModel.createLayout(newLayoutName)
                        newLayoutName = ""
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("Add")
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(Modifier.fillMaxSize()) {
            items(layouts) { layout ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { viewModel.switchLayout(layout.id) }
                ) {
                    val color = if (layout.isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = layout.name, color = color, style = MaterialTheme.typography.titleMedium)
                        if (layout.isSelected) {
                            Text("Active", color = color, style = MaterialTheme.typography.labelLarge)
                        } else {
                            TextButton(onClick = { viewModel.deleteLayout(layout.id) }) {
                                Text("Delete", color = androidx.compose.ui.graphics.Color(0xFFE57373))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SystemTTSView(viewModel: GloveViewModel) {
    val context = LocalContext.current
    val pitch by viewModel.prefs.pitchFlow.collectAsState(initial = 1.0f)
    val speed by viewModel.prefs.speedFlow.collectAsState(initial = 1.0f)
    
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("TTS Voice Pitch: ${"%.1f".format(pitch)}x", color = MaterialTheme.colorScheme.onBackground)
        Slider(value = pitch, onValueChange = { viewModel.setPitch(it) }, valueRange = 0.5f..2.0f, steps = 15)
        
        Spacer(Modifier.height(16.dp))
        
        Text("TTS Voice Speed: ${"%.1f".format(speed)}x", color = MaterialTheme.colorScheme.onBackground)
        Slider(value = speed, onValueChange = { viewModel.setSpeed(it) }, valueRange = 0.5f..2.0f, steps = 15)
        
        Spacer(Modifier.height(32.dp))
        
        Button(
            onClick = {
                val intent = Intent("com.android.settings.TTS_SETTINGS")
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open System TTS Settings", color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}
