package com.simpletools.networkinspector.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simpletools.networkinspector.feature.history.HistoryScreen
import com.simpletools.networkinspector.feature.lan.LanDiscoveryScreen
import com.simpletools.networkinspector.feature.settings.SettingsScreen
import com.simpletools.networkinspector.MainActivity.DashboardRoute

object Routes {
    const val Dashboard = "dashboard"
    const val History = "history"
    const val Lan = "lan"
    const val Settings = "settings"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Dashboard) {
        composable(Routes.Dashboard) {
            DashboardRoute(
                onHistory = { navController.navigate(Routes.History) },
                onLan = { navController.navigate(Routes.Lan) },
                onSettings = { navController.navigate(Routes.Settings) }
            )
        }
        composable(Routes.History) { HistoryScreen() }
        composable(Routes.Lan) { LanDiscoveryScreen() }
        composable(Routes.Settings) { SettingsScreen() }
    }
}
