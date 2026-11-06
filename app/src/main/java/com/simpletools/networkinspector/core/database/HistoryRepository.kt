package com.simpletools.networkinspector.core.database

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

class HistoryRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "network_inspector.db"
    ).build()

    private val dao = db.diagnosticDao()

    fun observeAll(): Flow<List<DiagnosticEntity>> = dao.observeAll()

    suspend fun save(entity: DiagnosticEntity) = dao.insert(entity)

    suspend fun delete(entity: DiagnosticEntity) = dao.delete(entity)

    suspend fun clear() = dao.deleteAll()
}
