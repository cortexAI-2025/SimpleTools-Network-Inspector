package com.simpletools.networkinspector.feature.wifi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simpletools.networkinspector.core.wifi.WifiInfoRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WifiAnalyzerViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = WifiInfoRepository(app)
    val snapshot = repo.snapshot.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val signal = repo.signalLevel(snapshot.value?.rssi)

    fun refresh() {
        viewModelScope.launch { repo.refresh() }
    }
}

@Composable
fun WifiAnalyzerScreen(viewModel: WifiAnalyzerViewModel = viewModel()) {
    val wifi by viewModel.snapshot.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Wi-Fi Analyzer")
        Button(onClick = { viewModel.refresh() }) { Text("Rafraîchir") }
        wifi?.let { w ->
            Text("SSID: ${w.ssid ?: "Unknown"}")
            Text("BSSID: ${w.bssid ?: "—"}")
            Text("RSSI: ${w.rssi} dBm")
            Text("Fréquence: ${w.frequency} MHz")
            Text("Bande: ${band(w.frequency)}")
            Text("Vitesse de liaison: ${w.linkSpeed} Mbps")
            Text("IP: ${w.ipAddress ?: "—"}")
        } ?: Text("Non connecté au Wi-Fi")
        Text("\nSeuils de signal indicatifs (non garantis de débit) :")
        Text("• Fort ≥ -55 dBm")
        Text("• Moyen ≥ -70 dBm")
        Text("• Faible < -70 dBm")
    }
}

private fun band(freq: Int?): String = when {
    freq == null -> "—"
    freq in 2400..2500 -> "2,4 GHz"
    freq in 5000..5900 -> "5 GHz"
    freq >= 5900 -> "6 GHz"
    else -> "Autre"
}
