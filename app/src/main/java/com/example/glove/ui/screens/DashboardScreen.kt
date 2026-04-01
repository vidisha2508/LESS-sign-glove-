package com.example.glove.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.glove.ui.GloveViewModel

enum class DashboardTab { STATUS, DEVICES, LAYOUTS, SETTINGS }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DashboardScreen(viewModel: GloveViewModel) {
    var selectedTab by remember { mutableStateOf(DashboardTab.STATUS) }

    Scaffold(
        containerColor = Color.Transparent, // Bubble up to allow main gradient background
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == DashboardTab.STATUS,
                    onClick = { selectedTab = DashboardTab.STATUS },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Status") },
                    label = { Text("Status") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        indicatorColor = MaterialTheme.colorScheme.surface
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == DashboardTab.DEVICES,
                    onClick = { selectedTab = DashboardTab.DEVICES },
                    icon = { Icon(Icons.Default.Refresh, contentDescription = "Devices") },
                    label = { Text("Devices") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        indicatorColor = MaterialTheme.colorScheme.surface
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == DashboardTab.LAYOUTS,
                    onClick = { selectedTab = DashboardTab.LAYOUTS },
                    icon = { Icon(Icons.Default.List, contentDescription = "Layouts") },
                    label = { Text("Layouts") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        indicatorColor = MaterialTheme.colorScheme.surface
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == DashboardTab.SETTINGS,
                    onClick = { selectedTab = DashboardTab.SETTINGS },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        indicatorColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "Tab Transition"
            ) { targetTab ->
                when (targetTab) {
                    DashboardTab.STATUS -> StatusTab(viewModel)
                    DashboardTab.DEVICES -> DevicesTab(viewModel)
                    DashboardTab.LAYOUTS -> LayoutsTab(viewModel)
                    DashboardTab.SETTINGS -> SettingsTab(viewModel)
                }
            }
        }
    }
}
