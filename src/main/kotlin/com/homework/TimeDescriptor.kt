package com.homework

@Suppress("unused")
enum class TimeDescriptor(private val minValue: Int, private val maxValue: Int, val label: String) {
    MINUTE(0, 59, "minute"),
    HOUR(0, 23, "hour"),
    DAY_OF_MONTH(1, 31, "day of month"),
    MONTH(1, 12, "month"),
    DAY_OF_WEEK(1, 7, "day of week");

    fun range() = minValue..maxValue
}
