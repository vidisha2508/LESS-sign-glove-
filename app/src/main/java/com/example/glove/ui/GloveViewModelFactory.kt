package com.example.glove.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.glove.bluetooth.GloveBluetoothManager
import com.example.glove.data.repository.GloveRepository
import com.example.glove.data.repository.PreferencesRepository
import com.example.glove.system.GloveHapticManager
import com.example.glove.tts.GloveTextToSpeechManager

class GloveViewModelFactory(
    private val repository: GloveRepository,
    private val bluetoothManager: GloveBluetoothManager,
    private val ttsManager: GloveTextToSpeechManager,
    private val prefs: PreferencesRepository,
    private val hapticManager: GloveHapticManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GloveViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GloveViewModel(repository, bluetoothManager, ttsManager, prefs, hapticManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
