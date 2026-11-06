package com.simpletools.networkinspector.core.export

import android.content.Context
import android.graphics.pdf.PdfDocument
import com.simpletools.networkinspector.core.database.DiagnosticEntity
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class ExportRepository(private val context: Context) {

    fun toCsv(diagnostics: List<DiagnosticEntity>): String {
        val header = "id,timestamp,type,network,method,status,durationMs,notes"
        val rows = diagnostics.joinToString("\n") { d ->
            "${d.id},${d.timestamp},${d.type},${d.networkName ?: ""},${d.method},${d.status},${d.durationMs},${d.notes ?: ""}"
        }
        return "$header\n$rows"
    }

    fun toJson(diagnostics: List<DiagnosticEntity>): String {
        val arr = org.json.JSONArray()
        diagnostics.forEach { d ->
            arr.put(JSONObject().apply {
                put("id", d.id)
                put("timestamp", d.timestamp)
                put("type", d.type)
                put("networkName", d.networkName)
                put("method", d.method)
                put("status", d.status)
                put("durationMs", d.durationMs)
                put("notes", d.notes)
                put("version", "1.0")
            })
        }
        return arr.toString(2)
    }

    fun toPdf(diagnostics: List<DiagnosticEntity>, file: File) {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = doc.startPage(pageInfo)
        val canvas = page.canvas
        var y = 40f
        canvas.drawText("SimpleTools Network Inspector — AIWORKPAY", 40f, y, android.graphics.Paint())
        y += 30f
        diagnostics.take(30).forEach { d ->
            canvas.drawText("${d.type} | ${d.status} | ${d.method}", 40f, y, android.graphics.Paint())
            y += 18f
            if (y > 800) return@forEach
        }
        doc.finishPage(page)
        FileOutputStream(file).use { doc.writeTo(it) }
        doc.close()
    }
}
