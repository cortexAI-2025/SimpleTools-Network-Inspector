package com.simpletools.networkinspector.core.wifi

import android.content.Context
import android.net.wifi.WifiManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class WifiSnapshot(
    val ssid: String?,
    val bssid: String?,
    val rssi: Int?,
    val frequency: Int?,
    val linkSpeed: Int?,
    val ipAddress: String?
)

class WifiInfoRepository(private val context: Context) {
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val _snapshot = MutableStateFlow<WifiSnapshot?>(null)
    val snapshot = _snapshot.asStateFlow()

    fun refresh() {
        val info = wifiManager.connectionInfo
        if (info == null || info.networkId == -1) {
            _snapshot.value = null
            return
        }
        val ssid = info.ssid?.removePrefix("\"")?.removeSuffix("\"")
        val ip = if (info.ipAddress != 0) {
            String.format("%d.%d.%d.%d",
                info.ipAddress and 0xff,
                info.ipAddress shr 8 and 0xff,
                info.ipAddress shr 16 and 0xff,
                info.ipAddress shr 24 and 0xff)
        } else null
        _snapshot.value = WifiSnapshot(
            ssid = if (ssid == "<unknown ssid>") null else ssid,
            bssid = info.bssid,
            rssi = info.rssi,
            frequency = info.frequency,
            linkSpeed = info.linkSpeed,
            ipAddress = ip
        )
    }

    fun signalLevel(rssi: Int?): String = when {
        rssi == null -> "Unknown"
        rssi >= -55 -> "Strong"
        rssi >= -70 -> "Medium"
        else -> "Weak"
    }
}
