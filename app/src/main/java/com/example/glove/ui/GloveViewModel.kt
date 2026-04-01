package com.example.glove.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glove.bluetooth.BluetoothConnectionState
import com.example.glove.bluetooth.GloveBluetoothManager
import com.example.glove.data.local.LayoutEntity
import com.example.glove.data.local.MappingEntity
import com.example.glove.data.repository.GloveRepository
import com.example.glove.data.repository.PreferencesRepository
import com.example.glove.parsing.GestureParser
import com.example.glove.system.GloveHapticManager
import com.example.glove.tts.GloveTextToSpeechManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ExportData(val layouts: List<LayoutEntity>, val mappings: List<MappingEntity>)

class GloveViewModel(
    private val repository: GloveRepository,
    val bluetoothManager: GloveBluetoothManager,
    private val ttsManager: GloveTextToSpeechManager,
    val prefs: PreferencesRepository,
    private val hapticManager: GloveHapticManager
) : ViewModel() {

    val allLayouts = repository.allLayouts.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val selectedLayout = repository.selectedLayout.stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _currentMappings = MutableStateFlow<List<MappingEntity>>(emptyList())
    val currentMappings: StateFlow<List<MappingEntity>> = _currentMappings.asStateFlow()

    private val _lastReceivedGesture = MutableStateFlow<Int?>(null)
    val lastReceivedGesture: StateFlow<Int?> = _lastReceivedGesture.asStateFlow()

    private val _lastSpokenWord = MutableStateFlow("")
    val lastSpokenWord: StateFlow<String> = _lastSpokenWord.asStateFlow()

    init {
        viewModelScope.launch {
            allLayouts.collect { layouts ->
                if (layouts.isEmpty()) {
                    repository.createNewLayout("Default Layout", isSelected = true)
                }
            }
        }

        viewModelScope.launch {
            selectedLayout.filterNotNull().collectLatest { layout ->
                repository.getMappingsForLayout(layout.id).collect { mappings ->
                    _currentMappings.value = mappings
                }
            }
        }

        viewModelScope.launch {
            bluetoothManager.incomingData.collect { dataString ->
                val gestureIndex = GestureParser.parseGesture(dataString)
                if (gestureIndex != null && gestureIndex in 0..31) {
                    _lastReceivedGesture.value = gestureIndex
                    handleIncomingGesture(gestureIndex)
                }
            }
        }

        // Apply TTS Settings on change
        viewModelScope.launch {
            prefs.pitchFlow.combine(prefs.speedFlow) { pitch, speed -> Pair(pitch, speed) }
                .collect { (pitch, speed) ->
                    ttsManager.setRateAndPitch(speed, pitch)
                }
        }
    }

    private suspend fun handleIncomingGesture(gestureIndex: Int) {
        val layout = selectedLayout.value ?: return
        val wordToSpeak = repository.getWordForGesture(layout.id, gestureIndex)
        if (wordToSpeak.isNotBlank()) {
            ttsManager.speak(wordToSpeak)
            _lastSpokenWord.value = wordToSpeak
            hapticManager.vibrateShort() // Haptic feedback on speak
        }
    }

    fun connectToDevice(macAddress: String) {
        viewModelScope.launch {
            bluetoothManager.connectToDevice(macAddress)
        }
    }

    fun startScan() {
        bluetoothManager.getPairedDevices()
    }

    fun switchLayout(layoutId: Int) {
        viewModelScope.launch { repository.selectLayout(layoutId) }
    }

    fun createLayout(name: String) {
        viewModelScope.launch { repository.createNewLayout(name, isSelected = true) }
    }

    fun updateMapping(mapping: MappingEntity, newWord: String) {
        viewModelScope.launch { repository.updateMappingWord(mapping, newWord) }
    }

    fun deleteLayout(layoutId: Int) {
        viewModelScope.launch { repository.deleteLayout(layoutId) }
    }

    // JSON Export
    suspend fun getExportJson(): String {
        val layouts = allLayouts.value
        val mappings = mutableListOf<MappingEntity>()
        for (layout in layouts) {
            mappings.addAll(repository.getMappingsForLayout(layout.id).first())
        }
        val data = ExportData(layouts, mappings)
        return Gson().toJson(data)
    }

    // JSON Import
    fun restoreFromJson(json: String) {
        viewModelScope.launch {
            try {
                val data = Gson().fromJson(json, ExportData::class.java)
                data.layouts.forEach { layout ->
                    val newId = repository.createNewLayout(layout.name, layout.isSelected)
                    val oldMappings = data.mappings.filter { it.layoutId == layout.id }
                    oldMappings.forEach { oldMap ->
                        mappingEntityReplacement(newId, oldMap.gestureIndex, oldMap.mappedWord)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    // Helper to replace matching gesture mapping directly via update rather than full insert
    private suspend fun mappingEntityReplacement(newLayoutId: Int, index: Int, word: String) {
        // Find existing mapping (auto generated from createNewLayout)
        val mappings = repository.getMappingsForLayout(newLayoutId).first()
        val target = mappings.find { it.gestureIndex == index }
        if (target != null) {
            repository.updateMappingWord(target, word)
        }
    }

    // Settings
    fun setPitch(p: Float) = viewModelScope.launch { prefs.setPitch(p) }
    fun setSpeed(s: Float) = viewModelScope.launch { prefs.setSpeed(s) }
    fun setDarkMode(b: Boolean) = viewModelScope.launch { prefs.setDarkMode(b) }

    override fun onCleared() {
        super.onCleared()
        bluetoothManager.closeConnection()
        ttsManager.shutdown()
    }
}
