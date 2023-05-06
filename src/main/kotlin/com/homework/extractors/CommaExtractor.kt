package com.homework.extractors

import com.homework.CronComponent

class CommaExtractor : Extractor {
    override fun char() = CronComponent.COMMA

    override fun extract(cronComponent: CronComponent): List<Int> {
        return cronComponent.split(char())
            .map { it.toInt() }
    }
}
