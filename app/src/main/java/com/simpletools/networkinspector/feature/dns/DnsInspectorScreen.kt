package com.simpletools.networkinspector.feature.dns

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
import com.simpletools.networkinspector.core.dns.DnsRepository
import com.simpletools.networkinspector.core.dns.DnsResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DnsInspectorViewModel : ViewModel() {
    private val repo = DnsRepository()
    private val _result = MutableStateFlow<DnsResult?>(null)
    val result = _result.asStateFlow()
    private val _running = MutableStateFlow(false)
    val running = _running.asStateFlow()

    fun resolve(host: String) {
        if (host.isBlank()) return
        viewModelScope.launch {
            _running.value = true
            try {
                _result.value = repo.resolve(host.trim())
            } finally {
                _running.value = false
            }
        }
    }
}

@Composable
fun DnsInspectorScreen(viewModel: DnsInspectorViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var host by remember { mutableStateOf("example.com") }
    val result by viewModel.result.collectAsState()
    val running by viewModel.running.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("DNS Inspector")
        Text("Résolution via le système Android (InetAddress).")
        Text("Le résolveur final peut être masqué par DNS privé, VPN ou configuration intermédiaire.")
        OutlinedTextField(value = host, onValueChange = { host = it }, label = { Text("Nom de domaine") })
        Button(onClick = { viewModel.resolve(host) }, enabled = !running) {
            Text(if (running) "Résolution…" else "Résoudre")
        }
        result?.let { r ->
            if (r.error != null) {
                Text("Erreur: ${r.error}")
            } else {
                Text("Durée: ${r.durationMs} ms")
                Text("IPv4: ${r.ipv4.joinToString().ifEmpty { "—" }}")
                Text("IPv6: ${r.ipv6.joinToString().ifEmpty { "—" }}")
            }
        }
    }
}
