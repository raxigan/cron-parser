package com.homework

class CronComponent(val value: String, val timeDescriptor: TimeDescriptor, var resolved: Boolean = false) {

    companion object {

        const val COMMA = ','
        const val DASH = '-'
        const val SLASH = '/'
        const val ASTERISK = '*'

        val CRON_REGEX = Regex("((\\d+,)+\\d+|(\\d+-\\d+)|\\*)(/\\d+)?|\\d+")
        val SPECIAL_CHARS = listOf(COMMA, ASTERISK, SLASH, DASH)
    }

    fun with(value: String) = CronComponent(value, timeDescriptor, resolved)

    fun split(char: Char) = value.split(char)

    fun isValid() = CRON_REGEX.matches(value.trim())

    fun contains(char: Char) = value.contains(char)

    fun containsSpecialChar() = SPECIAL_CHARS.any { value.contains(it) }

    fun resolve() {
        resolved = true
    }

    fun invalidFormatMessage() = "Invalid cron expression format: '$value'"

    fun outOfRangeMessage() =
        "Field '${timeDescriptor.label}' $value contains value out of ${timeDescriptor.range()} range"
}
