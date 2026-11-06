package com.simpletools.networkinspector.core.diagnostics

import com.simpletools.networkinspector.core.database.DiagnosticEntity
import com.simpletools.networkinspector.core.database.HistoryRepository
import org.json.JSONObject

class DiagnosticSaver(private val history: HistoryRepository) {
    suspend fun saveAutomatic(result: AutomaticResult, networkName: String?) {
        val json = JSONObject().apply {
            put("overall", result.overallStatus)
            put("steps", result.steps.map { s ->
                JSONObject().apply {
                    put("name", s.name)
                    put("method", s.method)
                    put("status", s.status)
                    put("detail", s.detail)
                }
            })
        }.toString()
        history.save(
            DiagnosticEntity(
                type = "Automatic",
                networkName = networkName,
                method = "Multi-step",
                resultJson = json,
                durationMs = result.steps.sumOf { it.durationMs },
                status = result.overallStatus
            )
        )
    }
}
