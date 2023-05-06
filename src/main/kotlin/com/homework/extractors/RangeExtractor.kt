package com.homework.extractors

import com.homework.CronComponent
import kotlin.math.max
import kotlin.math.min

class RangeExtractor : Extractor {
    override fun char() = CronComponent.DASH

    override fun extract(cronComponent: CronComponent): List<Int> {
        val rangeComponents = cronComponent.split(char())
        val start = rangeComponents[0].toInt()
        val end = rangeComponents[1].toInt()

        return (min(start, end)..max(start, end)).toList()
    }
}
