package com.simpletools.networkinspector.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "diagnostics")
data class DiagnosticEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val type: String,
    val networkName: String?,
    val method: String,
    val resultJson: String,
    val durationMs: Long,
    val status: String,
    val notes: String? = null
)
