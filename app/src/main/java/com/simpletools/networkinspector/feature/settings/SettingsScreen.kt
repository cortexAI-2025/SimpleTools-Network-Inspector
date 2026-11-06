package com.simpletools.networkinspector.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Paramètres")
        Text("Thème : clair / sombre / système (géré par Material 3)")
        Text("Langues : français et anglais (ressources à compléter)")
        Text("\nConfidentialité")
        Text("• Aucun compte, aucune télémétrie, aucun backend.")
        Text("• Historique conservé uniquement en local.")
        Text("• Export volontaire uniquement.")
        Text("• Permissions demandées contextuellement.")
        Text("\nVersion 1.0.0 — SimpleTools Network Inspector by AIWORKPAY")
    }
}
