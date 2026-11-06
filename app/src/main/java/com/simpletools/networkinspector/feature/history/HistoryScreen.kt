package com.simpletools.networkinspector.feature.history

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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = viewModel()) {
    val items by viewModel.diagnostics.collectAsState()
    val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Historique local")
        Button(onClick = { viewModel.clearAll() }) { Text("Supprimer tout") }
        items.forEach { d ->
            Text("${fmt.format(Date(d.timestamp))} — ${d.type} — ${d.status} (${d.method})")
        }
        if (items.isEmpty()) Text("Aucun diagnostic enregistré.")
    }
}
