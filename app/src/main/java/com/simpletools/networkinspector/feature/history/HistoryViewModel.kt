package com.simpletools.networkinspector.feature.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.simpletools.networkinspector.core.database.DiagnosticEntity
import com.simpletools.networkinspector.core.database.HistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = HistoryRepository(app)

    val diagnostics = repo.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun delete(entity: DiagnosticEntity) {
        viewModelScope.launch { repo.delete(entity) }
    }

    fun clearAll() {
        viewModelScope.launch { repo.clear() }
    }
}
