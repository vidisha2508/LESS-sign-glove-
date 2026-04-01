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
import com.example.glove.ui.GloveViewModel
import com.example.glove.ui.components.GlassCard

@Composable
fun LayoutManagerScreen(
    viewModel: GloveViewModel,
    onBack: () -> Unit
) {
    val layouts by viewModel.allLayouts.collectAsState()
    var newLayoutName by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("Back", color = Color.White) }
            Spacer(Modifier.width(16.dp))
            Text("Layout Profiles", color = Color.White, style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.height(16.dp))
        
        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = newLayoutName,
                onValueChange = { newLayoutName = it },
                label = { Text("New Profile Name", color = Color.White.copy(0.7f)) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF64B5F6),
                    unfocusedBorderColor = Color.White.copy(0.4f)
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
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
            ) {
                Text("Add", color = Color.Black)
            }
        }

        Spacer(Modifier.height(24.dp))

        LazyColumn(Modifier.fillMaxSize()) {
            items(layouts) { layout ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        .clickable { viewModel.switchLayout(layout.id) }
                ) {
                    val color = if (layout.isSelected) Color(0xFF4DD0E1) else Color.White
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
