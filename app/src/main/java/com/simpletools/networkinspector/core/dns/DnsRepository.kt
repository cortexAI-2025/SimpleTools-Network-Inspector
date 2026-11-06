package com.simpletools.networkinspector.core.dns

import java.net.InetAddress
import kotlin.system.measureTimeMillis

data class DnsResult(
    val host: String,
    val ipv4: List<String>,
    val ipv6: List<String>,
    val durationMs: Long,
    val error: String?
)

class DnsRepository {
    fun resolve(host: String): DnsResult {
        return try {
            var result: DnsResult? = null
            val duration = measureTimeMillis {
                val addresses = InetAddress.getAllByName(host)
                val v4 = addresses.filter { it.hostAddress?.contains('.') == true }.map { it.hostAddress!! }
                val v6 = addresses.filter { it.hostAddress?.contains(':') == true }.map { it.hostAddress!! }
                result = DnsResult(host, v4, v6, 0, null)
            }
            result!!.copy(durationMs = duration)
        } catch (e: Exception) {
            DnsResult(host, emptyList(), emptyList(), 0, e.message ?: "Resolution failed")
        }
    }
}
