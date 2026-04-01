package com.example.glove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.glove.bluetooth.GloveBluetoothManager
import com.example.glove.data.local.GloveDatabase
import com.example.glove.data.repository.GloveRepository
import com.example.glove.data.repository.PreferencesRepository
import com.example.glove.system.GloveHapticManager
import com.example.glove.tts.GloveTextToSpeechManager
import com.example.glove.ui.GloveViewModel
import com.example.glove.ui.GloveViewModelFactory
import com.example.glove.ui.screens.GloveAppNavHost
import com.example.glove.ui.theme.GloveTheme

class MainActivity : ComponentActivity() {

    private lateinit var bluetoothManager: GloveBluetoothManager
    private lateinit var ttsManager: GloveTextToSpeechManager
    private lateinit var prefs: PreferencesRepository
    private lateinit var hapticManager: GloveHapticManager

    private val viewModel: GloveViewModel by viewModels {
        val db = GloveDatabase.getDatabase(applicationContext)
        val repository = GloveRepository(db.gloveDao())
        GloveViewModelFactory(repository, bluetoothManager, ttsManager, prefs, hapticManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothManager = GloveBluetoothManager(applicationContext)
        ttsManager = GloveTextToSpeechManager(applicationContext)
        prefs = PreferencesRepository(applicationContext)
        hapticManager = GloveHapticManager(applicationContext)

        setContent {
            val isDark by prefs.darkModeFlow.collectAsState(initial = true)
            
            GloveTheme(darkTheme = isDark) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = if (isDark) {
                                Brush.verticalGradient(listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)))
                            } else {
                                Brush.verticalGradient(listOf(Color(0xFFE0EAFC), Color(0xFFCFDEF3)))
                            }
                        )
                ) {
                    GloveAppNavHost(viewModel = viewModel)
                }
            }
        }
    }
}
