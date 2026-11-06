package com.simpletools.networkinspector.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NetworkSnapshot(
    val isConnected: Boolean,
    val transport: String,
    val isValidated: Boolean,
    val hasInternet: Boolean,
    val isMetered: Boolean,
    val isVpn: Boolean,
    val ipv4: String?,
    val ipv6: String?,
    val gateway: String?,
    val dnsServers: List<String>,
    val mtu: Int?
)

class NetworkInfoRepository(private val context: Context) {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _snapshot = MutableStateFlow<NetworkSnapshot?>(null)
    val snapshot: Flow<NetworkSnapshot?> = _snapshot.asStateFlow()

    fun refresh() {
        val network = cm.activeNetwork
        if (network == null) {
            _snapshot.value = NetworkSnapshot(false, "none", false, false, false, false, null, null, null, emptyList(), null)
            return
        }
        val caps = cm.getNetworkCapabilities(network)
        val lp = cm.getLinkProperties(network)
        _snapshot.value = buildSnapshot(network, caps, lp)
    }

    private fun buildSnapshot(network: Network, caps: NetworkCapabilities?, lp: LinkProperties?): NetworkSnapshot {
        val transport = when {
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "Wi-Fi"
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "Cellular"
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> "Ethernet"
            else -> "Other"
        }
        val ipv4 = lp?.linkAddresses?.firstOrNull { it.address.hostAddress?.contains('.') == true }?.address?.hostAddress
        val ipv6 = lp?.linkAddresses?.firstOrNull { it.address.hostAddress?.contains(':') == true }?.address?.hostAddress
        val gateway = lp?.routes?.firstOrNull { it.isDefaultRoute }?.gateway?.hostAddress
        val dns = lp?.dnsServers?.mapNotNull { it.hostAddress } ?: emptyList()
        return NetworkSnapshot(
            isConnected = true,
            transport = transport,
            isValidated = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true,
            hasInternet = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true,
            isMetered = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) == false,
            isVpn = caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true,
            ipv4 = ipv4,
            ipv6 = ipv6,
            gateway = gateway,
            dnsServers = dns,
            mtu = lp?.mtu
        )
    }
}
