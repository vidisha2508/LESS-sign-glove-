package com.example.glove.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import com.example.glove.ui.GloveViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: GloveViewModel,
    onNavigateForward: () -> Unit
) {
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        // Smooth scale-in animation
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(durationMillis = 1500)
        )
    }

    LaunchedEffect(key1 = true) {
        // Smooth fade-in animation
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        
        // Hold for ~2.5 seconds total to let user read logo and allow viewModel to prep logic
        delay(1500)
        
        // Check BT readiness optionally
        val isBtEnabled = viewModel.bluetoothManager.isBluetoothEnabled() 
        // We will pass this flow logically in step 2.

        onNavigateForward()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "GLOVE",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}
