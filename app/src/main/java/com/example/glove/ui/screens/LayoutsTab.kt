package com.example.glove.ui.screens

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
import com.example.glove.parsing.GestureParser
import com.example.glove.ui.GloveViewModel
import com.example.glove.ui.components.GlassCard

@Composable
fun LayoutsTab(viewModel: GloveViewModel) {
    var isManagingProfiles by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (isManagingProfiles) "Profiles" else "Gestures", 
                style = MaterialTheme.typography.headlineMedium, 
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(
                onClick = { isManagingProfiles = !isManagingProfiles },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(if (isManagingProfiles) "Edit Macros >" else "Switch Profile >")
            }
        }
        Spacer(Modifier.height(16.dp))

        if (isManagingProfiles) {
            LayoutManagerViewInternal(viewModel) 
        } else {
            MappingEditorViewInternal(viewModel) 
        }
    }
}

@Composable
fun MappingEditorViewInternal(viewModel: GloveViewModel) {
    val mappings by viewModel.currentMappings.collectAsState()
    val currentLayout by viewModel.selectedLayout.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Text("Editing Workspace: ${currentLayout?.name ?: "None"}", color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f))
        Spacer(Modifier.height(16.dp))
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
fun LayoutManagerViewInternal(viewModel: GloveViewModel) {
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
                    val color = if (layout.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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
                                Text("Delete", color = Color(0xFFE57373))
                            }
                        }
                    }
                }
            }
        }
    }
}
