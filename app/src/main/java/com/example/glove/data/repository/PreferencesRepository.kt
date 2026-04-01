package com.example.glove.data.repository

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesRepository(private val context: Context) {
    companion object {
        val PITCH_KEY = floatPreferencesKey("tts_pitch")
        val SPEED_KEY = floatPreferencesKey("tts_speed")
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    val pitchFlow: Flow<Float> = context.dataStore.data.map { it[PITCH_KEY] ?: 1.0f }
    val speedFlow: Flow<Float> = context.dataStore.data.map { it[SPEED_KEY] ?: 1.0f }
    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { it[DARK_MODE_KEY] ?: true }

    suspend fun setPitch(pitch: Float) { context.dataStore.edit { it[PITCH_KEY] = pitch } }
    suspend fun setSpeed(speed: Float) { context.dataStore.edit { it[SPEED_KEY] = speed } }
    suspend fun setDarkMode(darkMode: Boolean) { context.dataStore.edit { it[DARK_MODE_KEY] = darkMode } }
}
