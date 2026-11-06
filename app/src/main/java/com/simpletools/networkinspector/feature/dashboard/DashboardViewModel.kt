package com.simpletools.networkinspector.feature.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.simpletools.networkinspector.core.network.NetworkInfoRepository
import com.simpletools.networkinspector.core.wifi.WifiInfoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(app: Application) : AndroidViewModel(app) {
    private val networkRepo = NetworkInfoRepository(app)
    private val wifiRepo = WifiInfoRepository(app)

    val uiState = combine(networkRepo.snapshot, wifiRepo.snapshot) { net, wifi ->
        DashboardUiState(net, wifi, wifiRepo.signalLevel(wifi?.rssi))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            networkRepo.refresh()
            wifiRepo.refresh()
        }
    }
}

data class DashboardUiState(
    val network: com.simpletools.networkinspector.core.network.NetworkSnapshot? = null,
    val wifi: com.simpletools.networkinspector.core.wifi.WifiSnapshot? = null,
    val signalLabel: String = ""
)
