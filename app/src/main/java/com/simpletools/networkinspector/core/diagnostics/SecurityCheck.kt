package com.simpletools.networkinspector.core.diagnostics

import com.simpletools.networkinspector.core.network.NetworkSnapshot
import com.simpletools.networkinspector.core.wifi.WifiSnapshot

data class SecurityObservation(
    val observation: String,
    val attentionLevel: String, // Info, Attention, Warning
    val explanation: String,
    val recommendation: String
)

class SecurityCheck {
    fun analyze(network: NetworkSnapshot?, wifi: WifiSnapshot?): List<SecurityObservation> {
        val list = mutableListOf<SecurityObservation>()

        if (network?.isVpn == true) {
            list.add(SecurityObservation(
                "VPN actif",
                "Info",
                "Certaines informations réseau peuvent différer de celles du lien physique.",
                "Vérifier que le VPN est intentionnel."
            ))
        }

        if (wifi != null && (wifi.ssid == null || wifi.ssid == "<unknown ssid>")) {
            list.add(SecurityObservation(
                "SSID non disponible",
                "Info",
                "Restriction Android ou permission manquante.",
                "Accorder la permission de localisation si nécessaire pour le SSID."
            ))
        }

        // Open network detection is limited without scan results; placeholder observation
        list.add(SecurityObservation(
            "Vérification de sécurité Wi-Fi",
            "Info",
            "Le type de sécurité exact nécessite un scan ou des permissions supplémentaires.",
            "Éviter les réseaux ouverts pour les communications sensibles."
        ))

        return list
    }
}
