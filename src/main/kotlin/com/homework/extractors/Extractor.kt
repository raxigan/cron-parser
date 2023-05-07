package com.homework.extractors

import com.homework.CronComponent
import com.homework.exitWithMessage

interface Extractor {

    fun char(): Char

    fun extract(cronComponent: CronComponent): List<Int>

    fun tryExtract(cronComponent: CronComponent): List<Int> {
        return if (!cronComponent.resolved)
            extractField(cronComponent).onEach { validateIsInRange(it, cronComponent) }
        else
            emptyList()
    }

    fun extractField(field: CronComponent): List<Int> {
        return if (field.containsSpecialChar()) {
            if (field.contains(char())) extractFieldWithSpecialChars(field) else emptyList()
        } else {
            extractSingleNumber(field)
        }
    }

    fun extractFieldWithSpecialChars(field: CronComponent): List<Int> {
        val extracted = extract(field)
        field.resolve()
        return extracted
    }

    private fun extractSingleNumber(field: CronComponent): List<Int> {
        val toInt = field.value.toInt()
        field.resolve()
        return listOf(toInt)
    }

    private fun validateIsInRange(value: Int, cron: CronComponent) {

        if (value !in cron.timeDescriptor.range()) {
            exitWithMessage(cron.outOfRangeMessage())
        }
    }
}
