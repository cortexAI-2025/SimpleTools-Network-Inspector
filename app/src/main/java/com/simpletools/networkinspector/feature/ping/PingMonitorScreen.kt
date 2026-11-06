package com.simpletools.networkinspector.feature.ping

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simpletools.networkinspector.core.diagnostics.PingRepository
import com.simpletools.networkinspector.core.diagnostics.PingStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PingMonitorViewModel : ViewModel() {
    private val repo = PingRepository()
    private val _stats = MutableStateFlow<PingStats?>(null)
    val stats = _stats.asStateFlow()
    private val _running = MutableStateFlow(false)
    val running = _running.asStateFlow()

    fun run(host: String, port: Int = 80) {
        viewModelScope.launch {
            _running.value = true
            try {
                _stats.value = repo.pingTcp(host, port, 4)
            } finally {
                _running.value = false
            }
        }
    }
}

@Composable
fun PingMonitorScreen(viewModel: PingMonitorViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var host by remember { mutableStateOf("1.1.1.1") }
    val stats by viewModel.stats.collectAsState()
    val running by viewModel.running.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Ping Monitor (TCP Connect)")
        Text("Protocole explicitement TCP — non ICMP.")
        OutlinedTextField(value = host, onValueChange = { host = it }, label = { Text("Hôte / IP") })
        Button(onClick = { viewModel.run(host) }, enabled = !running) {
            Text(if (running) "Test en cours…" else "Lancer le test")
        }
        stats?.let { s ->
            Text("Protocole: ${s.protocol} port ${s.port}")
            Text("Min: ${s.min} ms | Max: ${s.max} ms | Moy: ${s.avg}")
            Text("Pertes: ${s.lossPercent} %")
        }
    }
}
