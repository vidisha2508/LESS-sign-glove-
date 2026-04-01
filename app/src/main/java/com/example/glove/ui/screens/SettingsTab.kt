package com.example.glove.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun SettingsTab(viewModel: GloveViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    val pitch by viewModel.prefs.pitchFlow.collectAsState(initial = 1.0f)
    val speed by viewModel.prefs.speedFlow.collectAsState(initial = 1.0f)
    val isDark by viewModel.prefs.darkModeFlow.collectAsState(initial = true)

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

    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Spacer(Modifier.height(16.dp))
        Text("System Settings", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(32.dp))
        
        Text("TTS Pitch: ${"%.1f".format(pitch)}x", color = MaterialTheme.colorScheme.onBackground)
        Slider(value = pitch, onValueChange = { viewModel.setPitch(it) }, valueRange = 0.5f..2.0f, steps = 15)
        
        Spacer(Modifier.height(16.dp))
        
        Text("TTS Speed: ${"%.1f".format(speed)}x", color = MaterialTheme.colorScheme.onBackground)
        Slider(value = speed, onValueChange = { viewModel.setSpeed(it) }, valueRange = 0.5f..2.0f, steps = 15)
        
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                val intent = Intent("com.android.settings.TTS_SETTINGS")
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Open System Text-to-Speech", color = MaterialTheme.colorScheme.onSecondary)
        }

        Spacer(Modifier.height(48.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode Theme", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
            Switch(checked = isDark, onCheckedChange = { viewModel.setDarkMode(it) })
        }

        Spacer(Modifier.height(48.dp))
        Text("Database Management", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { exportLauncher.launch("glove_profiles_export.json") }, modifier = Modifier.weight(1f)) {
                Text("Export JSON")
            }
            Button(onClick = { importLauncher.launch(arrayOf("application/json")) }, modifier = Modifier.weight(1f)) {
                Text("Import JSON")
            }
        }
        Spacer(Modifier.height(64.dp))
    }
}
