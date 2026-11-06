package com.simpletools.networkinspector.core.lan

import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class LanDevice(
    val ip: String,
    val hostname: String?,
    val responded: Boolean,
    val method: String,
    val portsTested: List<Int>
)

class LanDiscoveryRepository {
    /**
     * Limited, non-intrusive discovery of the local IPv4 subnet.
     * Requires explicit user confirmation before calling.
     * Never scans public Internet. Concurrency and timeouts are limited.
     * Absence of response does not mean the device is offline.
     */
    suspend fun discover(
        subnetBase: String, // e.g. "192.168.1"
        start: Int = 1,
        end: Int = 254,
        ports: List<Int> = listOf(80, 443, 22),
        timeoutMs: Int = 300,
        maxConcurrency: Int = 16
    ): List<LanDevice> = withContext(Dispatchers.IO) {
        val devices = mutableListOf<LanDevice>()
        val range = (start.coerceAtLeast(1)..end.coerceAtMost(254))
        // Simple sequential for safety in V1; can be parallelized with limited concurrency later
        for (i in range) {
            val ip = "$subnetBase.$i"
            if (!isPrivateIpv4(ip)) continue
            val hostname = try {
                InetAddress.getByName(ip).canonicalHostName.takeIf { it != ip }
            } catch (_: Exception) { null }
            var responded = false
            val tested = mutableListOf<Int>()
            for (port in ports) {
                tested.add(port)
                try {
                    Socket().use { s ->
                        s.connect(InetSocketAddress(ip, port), timeoutMs)
                        responded = true
                    }
                } catch (_: Exception) { /* timeout or refused */ }
            }
            if (responded || hostname != null) {
                devices.add(LanDevice(ip, hostname, responded, "TCP Connect", tested))
            }
        }
        devices
    }

    private fun isPrivateIpv4(ip: String): Boolean {
        val parts = ip.split(".").mapNotNull { it.toIntOrNull() }
        if (parts.size != 4) return false
        return when (parts[0]) {
            10 -> true
            172 -> parts[1] in 16..31
            192 -> parts[1] == 168
            else -> false
        }
    }
}
