package com.simpletools.networkinspector.feature.lan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.simpletools.networkinspector.core.lan.LanDiscoveryRepository
import com.simpletools.networkinspector.core.lan.LanDevice
import com.simpletools.networkinspector.core.network.NetworkInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LanDiscoveryViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = LanDiscoveryRepository()
    private val networkRepo = NetworkInfoRepository(app)

    private val _devices = MutableStateFlow<List<LanDevice>>(emptyList())
    val devices = _devices.asStateFlow()

    private val _running = MutableStateFlow(false)
    val running = _running.asStateFlow()

    private val _proposedRange = MutableStateFlow<String?>(null)
    val proposedRange = _proposedRange.asStateFlow()

    init {
        proposeRange()
    }

    fun proposeRange() {
        networkRepo.refresh()
        val ipv4 = networkRepo.snapshot.value?.ipv4
        _proposedRange.value = ipv4?.substringBeforeLast(".")?.let { "$it.1 – $it.254" }
    }

    /** Must be called only after explicit user confirmation. */
    fun startDiscovery() {
        val base = networkRepo.snapshot.value?.ipv4?.substringBeforeLast(".") ?: return
        viewModelScope.launch {
            _running.value = true
            try {
                _devices.value = repo.discover(base)
            } finally {
                _running.value = false
            }
        }
    }

    fun cancel() {
        // In V1 sequential; a Job can be added later for true cancellation
        _running.value = false
    }
}
