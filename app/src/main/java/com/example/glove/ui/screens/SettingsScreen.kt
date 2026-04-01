package com.example.glove.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.glove.ui.GloveViewModel
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun SettingsScreen(
    viewModel: GloveViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    val pitch by viewModel.prefs.pitchFlow.collectAsState(initial = 1.0f)
    val speed by viewModel.prefs.speedFlow.collectAsState(initial = 1.0f)
    val isDark by viewModel.prefs.darkModeFlow.collectAsState(initial = true)

    // Export Launcher
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                val json = viewModel.getExportJson()
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(json.toByteArray())
                }
            }
        }
    }

    // Import Launcher
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val jsonString = reader.readText()
                    viewModel.restoreFromJson(jsonString)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("Back", color = MaterialTheme.colorScheme.onBackground) }
            Spacer(Modifier.width(16.dp))
            Text("Settings & Config", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleLarge)
        }
        
        Spacer(Modifier.height(32.dp))
        
        Text("TTS Pitch: ${"%.1f".format(pitch)}x", color = MaterialTheme.colorScheme.onBackground)
        Slider(
            value = pitch,
            onValueChange = { viewModel.setPitch(it) },
            valueRange = 0.5f..2.0f,
            steps = 15
        )
        
        Spacer(Modifier.height(24.dp))
        
        Text("TTS Speed: ${"%.1f".format(speed)}x", color = MaterialTheme.colorScheme.onBackground)
        Slider(
            value = speed,
            onValueChange = { viewModel.setSpeed(it) },
            valueRange = 0.5f..2.0f,
            steps = 15
        )

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, 
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode Theme", color = MaterialTheme.colorScheme.onBackground)
            Switch(checked = isDark, onCheckedChange = { viewModel.setDarkMode(it) })
        }

        Spacer(Modifier.height(48.dp))

        Text("Data Management", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { exportLauncher.launch("glove_profiles_export.json") }) {
                Text("Export JSON")
            }
            Button(onClick = { importLauncher.launch(arrayOf("application/json")) }) {
                Text("Import JSON")
            }
        }
    }
}
