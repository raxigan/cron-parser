package com.homework

import com.homework.extractors.AsteriskExtractor
import com.homework.extractors.CommaExtractor
import com.homework.extractors.RangeExtractor
import com.homework.extractors.StepExtractor


object FieldParser {

    fun parse(field: CronComponent): List<Int> {

        if (!field.isValid()) {
            exitWithMessage(field.invalidFormatMessage())
        }

        val extractors = listOf(
            StepExtractor(),
            CommaExtractor(),
            RangeExtractor(),
            AsteriskExtractor()
        )

        return extractors
            .map { it.tryExtract(field) }
            .flatten()
    }
}
