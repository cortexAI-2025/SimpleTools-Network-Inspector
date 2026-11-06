package com.simpletools.networkinspector.feature.lan

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

@Composable
fun LanDiscoveryScreen(viewModel: LanDiscoveryViewModel = viewModel()) {
    val devices by viewModel.devices.collectAsState()
    val running by viewModel.running.collectAsState()
    val range by viewModel.proposedRange.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("LAN Discovery")
        Text("Plage proposée : ${range ?: "Indisponible"}")
        Text("Confirmation requise. Scan limité au sous-réseau local privé uniquement.")
        Text("L’absence de réponse ne signifie pas que l’équipement est éteint.")

        if (!running) {
            Button(onClick = { viewModel.startDiscovery() }) {
                Text("Confirmer et démarrer la découverte")
            }
        } else {
            Button(onClick = { viewModel.cancel() }) { Text("Annuler") }
            Text("Découverte en cours…")
        }

        devices.forEach { d ->
            Text("${d.ip} — ${d.hostname ?: "?"} — ${if (d.responded) "Répondu" else "Hostname uniquement"} (${d.method})")
        }
    }
}
