package com.example.glove.ui.screens

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
fun MappingEditorScreen(
    viewModel: GloveViewModel,
    onBack: () -> Unit
) {
    val mappings by viewModel.currentMappings.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("Back", color = Color.White) }
            Spacer(Modifier.width(16.dp))
            Text("Edit Mappings", color = Color.White, style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.height(16.dp))
        
        LazyColumn(Modifier.fillMaxSize()) {
            items(mappings) { mapping ->
                // Keeping state bound securely for typing performance without DB lag
                var textValue by remember(mapping.mappedWord) { mutableStateOf(mapping.mappedWord) }
                
                GlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = GestureParser.intToBinaryString(mapping.gestureIndex),
                            color = Color(0xFF4DD0E1),
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
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color(0xFF64B5F6),
                                focusedBorderColor = Color(0xFF64B5F6),
                                unfocusedBorderColor = Color.White.copy(0.4f)
                            ),
                            singleLine = true
                        )
                    }
                }
            }
        }
    }
}
