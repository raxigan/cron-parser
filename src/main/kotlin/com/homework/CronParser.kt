package com.homework

import kotlin.system.exitProcess


object CronParser {
    const val ERROR_CODE = 1
    const val FIELD_NAME_COLUMN_SIZE = 14
}

fun parse(args: Array<String>) {

    val timeFieldsNo = FieldDescriptor.values().size

    if (args.isEmpty() || args.contains("--help") || args.contains("-h")) {
        println("Usage: cron_parser.kts <cron_expression>")
        exitWithError()
        return
    }

    val cronString = args.first()
    val cronComponents = cronString.split("\\s+".toRegex())

    if (cronComponents.size < timeFieldsNo + 1) {
        println("Too few cron components provided. Please provide a valid cron string.")
        exitWithError()
        return
    }

    val command = cronComponents.drop(timeFieldsNo).joinToString(" ")

    val sb = StringBuilder()

    printTimes(sb, cronComponents)
    printCommand(sb, command)

    println(sb)
}

fun exitWithError() {
    exitProcess(CronParser.ERROR_CODE)
}

fun printCommand(stringBuilder: StringBuilder, command: String) {
    stringBuilder.append("command".padEnd(CronParser.FIELD_NAME_COLUMN_SIZE) + command)
}

fun printTimes(stringBuilder: StringBuilder, cronComponents: List<String>) {
    FieldDescriptor.values().withIndex()
        .forEach {
            stringBuilder.append(
                "${it.value.desc.padEnd(CronParser.FIELD_NAME_COLUMN_SIZE)}${
                    FieldParser().parseField(
                        cronComponents[it.index],
                        it.value
                    ).joinToString(" ")
                }\n"
            )
        }
}

fun validateIsInRange(value: Int, fieldDescriptor: FieldDescriptor) {

    val validRange = fieldDescriptor.minValue..fieldDescriptor.maxValue

    if (value !in validRange) {
        println("Field '${fieldDescriptor.desc}' $value out of $validRange range")
        exitWithError()
    }
}


