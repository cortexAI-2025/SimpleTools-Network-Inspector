package com.simpletools.networkinspector.core.charts

/**
 * Lightweight in-memory series for charts.
 * Acquisition is separated from rendering.
 */
data class ChartPoint(val timestamp: Long, val value: Double?)

class SimpleChartSeries(private val maxPoints: Int = 60) {
    private val points = ArrayDeque<ChartPoint>()

    fun add(value: Double?) {
        if (points.size >= maxPoints) points.removeFirst()
        points.addLast(ChartPoint(System.currentTimeMillis(), value))
    }

    fun snapshot(): List<ChartPoint> = points.toList()

    fun clear() = points.clear()
}
