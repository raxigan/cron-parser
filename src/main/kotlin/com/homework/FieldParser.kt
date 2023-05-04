package com.homework

import kotlin.math.max
import kotlin.math.min

class FieldParser {

    fun parseField(field: String, fieldDescriptor: FieldDescriptor, res: List<Int> = emptyList()): List<Int> {

        val all = Regex("((\\d+,)+\\d+|(\\d+-\\d+)|\\*)(/\\d+)?|\\d+")

        if (!all.matches(field.trim())) {
            println("Invalid cron format on component '$field'")
            exitWithError()
        }

        return if (field.contains("/")) {
            res + extractWithStep(field, fieldDescriptor)
        } else if (field.contains(",")) {
            res + extractCommaRange(field, fieldDescriptor)
        } else if (field.contains("-")) {
            res + extractDashRange(field, fieldDescriptor)
        } else if (field.contains("*")) {
            res + (fieldDescriptor.minValue..fieldDescriptor.maxValue)
        } else {
            res + extractNumber(field, fieldDescriptor)
        }
    }

    private fun extractNumber(field: String, fieldDescriptor: FieldDescriptor): Int {
        val toInt = field.toInt()
        validateIsInRange(toInt, fieldDescriptor)
        return toInt
    }

    private fun extractWithStep(field: String, fieldDescriptor: FieldDescriptor):List<Int> {
        val split = field.split("/")
        val range = parseField(split[0], fieldDescriptor)
        val interval = split[1].toInt()
        return (range.indices step (interval)).map { range[it] }
    }

    private fun extractDashRange(field:String, fieldDescriptor: FieldDescriptor): List<Int> {
        val range = field.split("-")
        val start = range[0].toInt()
        val end = range[1].toInt()

        val rng = min(start, end)..max(start, end)

        validateIsInRange(start, fieldDescriptor)
        validateIsInRange(end, fieldDescriptor)

        return rng.toList()
    }

    private fun extractCommaRange(
        field: String,
        fieldDescriptor: FieldDescriptor
    ) = field.split(",")
        .map { it.toInt() }
        .onEach { validateIsInRange(it, fieldDescriptor) }
}
