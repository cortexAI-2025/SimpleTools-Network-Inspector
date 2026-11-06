package com.simpletools.networkinspector.feature.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.simpletools.networkinspector.core.diagnostics.AutomaticDiagnostic
import com.simpletools.networkinspector.core.diagnostics.PingRepository
import com.simpletools.networkinspector.core.diagnostics.SecurityCheck
import com.simpletools.networkinspector.core.dns.DnsRepository
import com.simpletools.networkinspector.core.network.NetworkInfoRepository
import com.simpletools.networkinspector.core.wifi.WifiInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(app: Application) : AndroidViewModel(app) {
    private val networkRepo = NetworkInfoRepository(app)
    private val wifiRepo = WifiInfoRepository(app)
    private val dnsRepo = DnsRepository()
    private val pingRepo = PingRepository()
    private val autoDiag = AutomaticDiagnostic(networkRepo, wifiRepo, dnsRepo, pingRepo)
    private val security = SecurityCheck()

    private val _autoResult = MutableStateFlow<com.simpletools.networkinspector.core.diagnostics.AutomaticResult?>(null)
    private val _security = MutableStateFlow<List<com.simpletools.networkinspector.core.diagnostics.SecurityObservation>>(emptyList())

    val uiState = combine(networkRepo.snapshot, wifiRepo.snapshot, _autoResult, _security) { net, wifi, auto, sec ->
        DashboardUiState(net, wifi, wifiRepo.signalLevel(wifi?.rssi), auto, sec)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            networkRepo.refresh()
            wifiRepo.refresh()
            _security.value = security.analyze(networkRepo.snapshot.value, wifiRepo.snapshot.value)
        }
    }

    fun runAutomatic() {
        viewModelScope.launch {
            _autoResult.value = autoDiag.run()
        }
    }
}

data class DashboardUiState(
    val network: com.simpletools.networkinspector.core.network.NetworkSnapshot? = null,
    val wifi: com.simpletools.networkinspector.core.wifi.WifiSnapshot? = null,
    val signalLabel: String = "",
    val autoResult: com.simpletools.networkinspector.core.diagnostics.AutomaticResult? = null,
    val security: List<com.simpletools.networkinspector.core.diagnostics.SecurityObservation> = emptyList()
)
