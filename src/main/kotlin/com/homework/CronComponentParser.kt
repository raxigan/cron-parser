package com.homework

import com.homework.extractors.AsteriskExtractor
import com.homework.extractors.CommaExtractor
import com.homework.extractors.RangeExtractor
import com.homework.extractors.StepExtractor


object CronComponentParser {

    fun parse(cronComponent: CronComponent): List<Int> {

        if (!cronComponent.isValid()) {
            exitWithMessage(cronComponent.invalidFormatMessage())
        }

        val extractors = listOf(
            StepExtractor(),
            CommaExtractor(),
            RangeExtractor(),
            AsteriskExtractor()
        )

        return extractors
            .map { it.tryExtract(cronComponent) }
            .flatten()
    }
}
