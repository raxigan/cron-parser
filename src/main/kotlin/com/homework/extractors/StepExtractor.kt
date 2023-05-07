package com.homework.extractors

import com.homework.CronComponent
import com.homework.CronComponentParser

class StepExtractor : Extractor {
    override fun char() = CronComponent.SLASH

    override fun extract(cronComponent: CronComponent): List<Int> {
        val split = cronComponent.split(char())
        val range = CronComponentParser.parse(cronComponent.with(split[0]))
        val interval = split[1].toInt()

        cronComponent.resolve()

        return (range.indices step (interval)).map { range[it] }
    }
}