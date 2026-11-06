package com.simpletools.networkinspector.core.diagnostics

import com.simpletools.networkinspector.core.dns.DnsRepository
import com.simpletools.networkinspector.core.network.NetworkInfoRepository
import com.simpletools.networkinspector.core.wifi.WifiInfoRepository

data class DiagnosticStep(
    val name: String,
    val method: String,
    val status: String, // Success, Failure, Unavailable, NotRun, PermissionMissing
    val detail: String,
    val durationMs: Long
)

data class AutomaticResult(
    val steps: List<DiagnosticStep>,
    val overallStatus: String
)

class AutomaticDiagnostic(
    private val networkRepo: NetworkInfoRepository,
    private val wifiRepo: WifiInfoRepository,
    private val dnsRepo: DnsRepository,
    private val pingRepo: PingRepository
) {
    fun run(): AutomaticResult {
        val steps = mutableListOf<DiagnosticStep>()

        // 1. Active network
        networkRepo.refresh()
        val net = networkRepo.snapshot.value
        steps.add(DiagnosticStep(
            "Active network",
            "ConnectivityManager",
            if (net?.isConnected == true) "Success" else "Failure",
            net?.transport ?: "None",
            0
        ))

        // 2. Wi-Fi details
        wifiRepo.refresh()
        val wifi = wifiRepo.snapshot.value
        steps.add(DiagnosticStep(
            "Wi-Fi info",
            "WifiManager",
            if (wifi != null) "Success" else "Unavailable",
            wifi?.ssid ?: "Not connected",
            0
        ))

        // 3. Gateway reachability (TCP)
        val gw = net?.gateway
        if (gw != null) {
            val ping = pingRepo.pingTcp(gw, 80, 2)
            steps.add(DiagnosticStep(
                "Gateway",
                "TCP Connect",
                if (ping.lossPercent < 100) "Success" else "Failure",
                "Loss ${ping.lossPercent}%", 
                0
            ))
        } else {
            steps.add(DiagnosticStep("Gateway", "N/A", "Unavailable", "No gateway", 0))
        }

        // 4. DNS
        val dnsResult = dnsRepo.resolve("example.com")
        steps.add(DiagnosticStep(
            "DNS resolution",
            "System InetAddress",
            if (dnsResult.error == null) "Success" else "Failure",
            dnsResult.error ?: "Resolved in ${dnsResult.durationMs} ms",
            dnsResult.durationMs
        ))

        val overall = if (steps.any { it.status == "Failure" }) "Issues detected" else "OK"
        return AutomaticResult(steps, overall)
    }
}
