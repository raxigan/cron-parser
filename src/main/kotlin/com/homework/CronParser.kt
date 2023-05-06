package com.homework

import kotlin.system.exitProcess

class CronParser {

    companion object {
        const val ERROR_CODE = 1
        const val FIELD_NAME_COLUMN_SIZE = 14
        const val COMMAND_FIELD_NAME = "command"

        const val USAGE_MESSAGE = "Usage: cron_parser.kts <cron_expression>"
        const val TOO_FEW_ARGUMENTS_MESSAGE = "Too few cron components provided. Please provide a valid cron string."
    }

    fun parse(args: Array<String>) {

        val timeFieldsNo = TimeDescriptor.values().size

        if (args.isEmpty() || args.contains("--help") || args.contains("-h")) {
            exitWithMessage(USAGE_MESSAGE)
        }

        val cronString = args.first()
        val cronComponents = cronString.split("\\s+".toRegex())

        if (cronComponents.size < timeFieldsNo + 1) {
            exitWithMessage(TOO_FEW_ARGUMENTS_MESSAGE)
        }

        val command = cronComponents.drop(timeFieldsNo).joinToString(" ")

        val timesOutput = timesOutput(cronComponents)
        val commandOutput = commandOutput(command)

        println(timesOutput)
        println(commandOutput)
    }

    private fun commandOutput(command: String): String {
        return COMMAND_FIELD_NAME.padEnd(FIELD_NAME_COLUMN_SIZE) + command
    }

    private fun timeOutputLine(fieldDesc: TimeDescriptor, fieldValue: String): String {
        return fieldDesc.desc.padEnd(FIELD_NAME_COLUMN_SIZE) +
                FieldParser.parse(CronComponent(fieldValue, fieldDesc)).joinToString(" ")
    }

    private fun timesOutput(cronComponents: List<String>): String {
        return TimeDescriptor.values()
            .withIndex()
            .joinToString(System.lineSeparator()) { timeOutputLine(it.value, cronComponents[it.index]) }
    }
}

fun exitWithError() {
    exitProcess(CronParser.ERROR_CODE)
}

fun exitWithMessage(message: String) {
    println(message)
    exitWithError()
}
