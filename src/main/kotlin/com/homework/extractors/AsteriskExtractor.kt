package com.homework.extractors

import com.homework.CronComponent

class AsteriskExtractor : Extractor {
    override fun char() = CronComponent.ASTERISK

    override fun extract(cronComponent: CronComponent): List<Int> {
        return cronComponent.timeDescriptor.range().toList()
    }
}
