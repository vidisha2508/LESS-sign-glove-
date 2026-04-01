package com.example.glove.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.glove.ui.GloveViewModel

@Composable
fun GloveAppNavHost(viewModel: GloveViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, 
        startDestination = "splash_screen",
        enterTransition = { fadeIn(animationSpec = tween(500)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) }
    ) {
        composable("splash_screen") {
            SplashScreen(
                viewModel = viewModel,
                onNavigateForward = {
                    navController.navigate("dashboard") {
                        popUpTo("splash_screen") { inclusive = true }
                    }
                }
            )
        }
        composable("dashboard") {
            DashboardScreen(viewModel = viewModel)
        }
    }
}
