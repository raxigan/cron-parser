package com.homework

class CronComponent(val value: String, val timeDescriptor: TimeDescriptor, var resolved: Boolean = false) {

    companion object {

        const val COMMA = ","
        const val DASH = "-"
        const val SLASH = "/"
        const val ASTERISK = "*"

        val CRON_REGEX = Regex("((\\d+,)+\\d+|(\\d+-\\d+)|\\*)(/\\d+)?|\\d+")
        val SPECIAL_CHARS = listOf<CharSequence>(COMMA, ASTERISK, SLASH, DASH)
    }

    fun with(value: String) = CronComponent(value, timeDescriptor, resolved)

    fun split(delimiters: String) = value.split(delimiters)

    fun isValid() = CRON_REGEX.matches(value.trim())

    fun contains(chars: CharSequence) = value.contains(chars)

    fun containsSpecialChar() = SPECIAL_CHARS.any { value.contains(it) }

    fun resolve() {
        resolved = true
    }

    fun invalidFormatMessage() = "Invalid cron expression format: '$value'"

    fun outOfRangeMessage() =
        "Field '${timeDescriptor.desc}' $value out of ${timeDescriptor.range()} range"
}