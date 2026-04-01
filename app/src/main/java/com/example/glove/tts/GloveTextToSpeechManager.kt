package com.example.glove.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class GloveTextToSpeechManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var lastSpokenWord = ""
    private var lastSpokenTime = 0L

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Can be configured to list locales later, US by default
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                isInitialized = false
            } else {
                isInitialized = true
                tts?.setSpeechRate(1.0f) // Normal speed
                tts?.setPitch(1.0f)      // Normal pitch
            }
        }
    }

    fun speak(word: String, debounceMs: Long = 1000L) {
        if (!isInitialized || word.isBlank()) return
        
        val currentTime = System.currentTimeMillis()
        
        // Debounce logic: don't rapidly repeat same word
        if (word == lastSpokenWord && (currentTime - lastSpokenTime) < debounceMs) {
            return 
        }

        // QUEUE_FLUSH reduces latency by interrupting current speech
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, "GLOVE_TTS")
        lastSpokenWord = word
        lastSpokenTime = currentTime
    }
    
    fun setRateAndPitch(rate: Float, pitch: Float) {
        tts?.setSpeechRate(rate)
        tts?.setPitch(pitch)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
