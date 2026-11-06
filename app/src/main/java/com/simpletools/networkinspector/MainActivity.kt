package com.simpletools.networkinspector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simpletools.networkinspector.feature.dashboard.DashboardViewModel
import com.simpletools.networkinspector.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun DashboardRoute(
    onHistory: () -> Unit,
    onLan: () -> Unit,
    onSettings: () -> Unit,
    onWifi: () -> Unit,
    onPing: () -> Unit,
    viewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Dashboard(
            state,
            onRefresh = { viewModel.refresh() },
            onAnalyze = { viewModel.runAutomatic() },
            onHistory = onHistory,
            onLan = onLan,
            onSettings = onSettings,
            onWifi = onWifi,
            onPing = onPing,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun Dashboard(
    state: com.simpletools.networkinspector.feature.dashboard.DashboardUiState,
    onRefresh: () -> Unit,
    onAnalyze: () -> Unit,
    onHistory: () -> Unit,
    onLan: () -> Unit,
    onSettings: () -> Unit,
    onWifi: () -> Unit,
    onPing: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("SimpleTools Network Inspector", style = MaterialTheme.typography.headlineMedium)
        Text("by AIWORKPAY — V1.0.0")
        Button(onClick = onRefresh) { Text("Refresh") }
        Button(onClick = onAnalyze) { Text("Analyser ma connexion") }
        Button(onClick = onWifi) { Text("Wi-Fi Analyzer") }
        Button(onClick = onPing) { Text("Ping Monitor") }
        Button(onClick = onHistory) { Text("Historique") }
        Button(onClick = onLan) { Text("LAN Discovery") }
        Button(onClick = onSettings) { Text("Paramètres") }

        state.network?.let { n ->
            Text("Transport: ${n.transport}")
            Text("Internet: ${if (n.hasInternet) "Yes" else "No"} (validated: ${n.isValidated})")
            Text("IPv4: ${n.ipv4 ?: "—"}")
            Text("Gateway: ${n.gateway ?: "—"}")
            Text("DNS: ${n.dnsServers.joinToString()}")
            Text("VPN: ${if (n.isVpn) "Active" else "No"}")
        }
        state.wifi?.let { w ->
            Text("SSID: ${w.ssid ?: "Unknown"}")
            Text("RSSI: ${w.rssi} dBm (${state.signalLabel})")
            Text("Frequency: ${w.frequency} MHz")
        } ?: Text("Wi-Fi: not connected")

        state.autoResult?.let { r ->
            Text("\nDiagnostic automatique: ${r.overallStatus}")
            r.steps.forEach { s ->
                Text("${s.name}: ${s.status} — ${s.detail} (${s.method})")
            }
        }

        if (state.security.isNotEmpty()) {
            Text("\nObservations de sécurité:")
            state.security.forEach { o ->
                Text("${o.attentionLevel}: ${o.observation}")
                Text(o.explanation)
                Text(o.recommendation)
            }
        }
    }
}
