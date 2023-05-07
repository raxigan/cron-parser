package com.homework

import kotlin.system.exitProcess

class CronExpressionParser {

    companion object {
        const val ERROR_CODE = 1
        const val FIELD_NAME_COLUMN_SIZE = 14
        const val COMMAND_FIELD_NAME = "command"
        val HELP_ARGUMENTS = listOf("--help", "-h", "help", "man")

        val USAGE_MESSAGE = """
            Usage: 
                ./cron_parser.kts <cron_expression>
            
            Example: 
                ./cron_parser.kts "*/15 0 1,15 * 1-5 /usr/bin/find"
        """.trimIndent()
        val TOO_FEW_ARGUMENTS_MESSAGE = """
            Too few cron components provided. Please provide 5 cron time components and a command to be executed.
            
            Example: 
                ./cron_parser.kts "*/15 0 1,15 * 1-5 /usr/bin/find"
        """.trimIndent()
    }

    fun parse(args: Array<String>) {

        val timeFieldsNo = TimeDescriptor.values().size

        if (args.isEmpty() || HELP_ARGUMENTS.any { args.contains(it) }) {
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

    private fun timeOutputLine(timeDescriptor: TimeDescriptor, fieldValue: String): String {
        return timeDescriptor.label.padEnd(FIELD_NAME_COLUMN_SIZE) +
                CronComponentParser.parse(CronComponent(fieldValue, timeDescriptor)).joinToString(" ")
    }

    private fun timesOutput(cronComponents: List<String>): String {
        return TimeDescriptor.values()
            .withIndex()
            .joinToString(System.lineSeparator()) { timeOutputLine(it.value, cronComponents[it.index]) }
    }
}

fun exitWithError() {
    exitProcess(CronExpressionParser.ERROR_CODE)
}

fun exitWithMessage(message: String) {
    println(message)
    exitWithError()
}
