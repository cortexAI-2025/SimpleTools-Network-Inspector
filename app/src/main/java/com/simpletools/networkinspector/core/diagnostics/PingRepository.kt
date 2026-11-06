package com.simpletools.networkinspector.core.diagnostics

import java.net.InetSocketAddress
import java.net.Socket
import kotlin.system.measureTimeMillis

data class PingAttempt(
    val success: Boolean,
    val latencyMs: Long?,
    val error: String?
)

data class PingStats(
    val host: String,
    val port: Int,
    val protocol: String, // "TCP" or "ICMP"
    val attempts: List<PingAttempt>,
    val min: Long?,
    val max: Long?,
    val avg: Double?,
    val lossPercent: Double
)

class PingRepository {
    /**
     * Performs a TCP connect test. Explicitly labeled as TCP, never presented as ICMP.
     * ICMP is not used because Android does not provide a reliable unprivileged ICMP API.
     */
    fun pingTcp(host: String, port: Int = 80, count: Int = 4, timeoutMs: Int = 3000): PingStats {
        val attempts = mutableListOf<PingAttempt>()
        repeat(count) {
            try {
                val latency = measureTimeMillis {
                    Socket().use { socket ->
                        socket.connect(InetSocketAddress(host, port), timeoutMs)
                    }
                }
                attempts.add(PingAttempt(true, latency, null))
            } catch (e: Exception) {
                attempts.add(PingAttempt(false, null, e.message))
            }
        }
        val successes = attempts.filter { it.success }.mapNotNull { it.latencyMs }
        val loss = (attempts.size - successes.size).toDouble() / attempts.size * 100
        return PingStats(
            host = host,
            port = port,
            protocol = "TCP",
            attempts = attempts,
            min = successes.minOrNull(),
            max = successes.maxOrNull(),
            avg = if (successes.isNotEmpty()) successes.average() else null,
            lossPercent = loss
        )
    }
}
