package com.homework

@Suppress("unused")
enum class FieldDescriptor(val minValue: Int, val maxValue: Int, val desc: String) {
    MINUTE(0, 59, "minute"),
    HOUR(0, 23, "hour"),
    DAY_OF_MONTH(1, 31, "day of month"),
    MONTH(1, 12, "month"),
    DAY_OF_WEEK(1, 7, "day of week")
}
