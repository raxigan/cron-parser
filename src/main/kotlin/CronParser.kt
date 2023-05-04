import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min
import kotlin.system.exitProcess


object CronParser {

    const val ERROR_CODE = 1

    fun exitWithError() {
        exitProcess(ERROR_CODE)
    }
}

fun parse(args: Array<String>) {

    if (args.isEmpty() || args.contains("--help") || args.contains("-h")) {
        println("Usage: cron_parser.kts <cron_string>")
        CronParser.exitWithError()
        return
    }

    val cronString = args[0]
    val cronComponents = cronString.split("\\s+".toRegex())

    if (cronComponents.size < 6) {
        println("Too few cron components provided. Please provide a valid cron string.")
        CronParser.exitWithError()
        return
    }

    val command = cronComponents.drop(5).joinToString(" ")

    val sb = StringBuilder()

    printTimes(sb, cronComponents)
    printCommand(sb, command)

    println(sb)
}

fun printCommand(stringBuilder: StringBuilder, command: String) {
    stringBuilder.append("command".padEnd(14) + command)
}

fun printTimes(stringBuilder: StringBuilder, cronComponents: List<String>) {
    FieldDescriptor.values().withIndex()
        .forEach {
            stringBuilder.append(
                "${it.value.desc.padEnd(14)}${
                    parseField(
                        cronComponents[it.index],
                        it.value
                    ).joinToString(" ")
                }\n"
            )
        }
}

enum class FieldDescriptor(val minValue: Int, val maxValue: Int, val desc: String) {
    MINUTE(0, 59, "minute"),
    HOUR(0, 23, "hour"),
    DAY_OF_MONTH(1, 31, "day of month"),
    MONTH(1, 12, "month"),
    DAY_OF_WEEK(1, 7, "day of week")
}

fun parseField(field: String, fieldDescriptor: FieldDescriptor, res: List<Int> = emptyList()): List<Int> {

    val all = Regex("((\\d+,)+\\d+|(\\d+-\\d+)|\\*)(/\\d+)?|\\d+")

    if (!all.matches(field.trim())) {
        println("Invalid cron format on component \"$field\"")
        CronParser.exitWithError()
    }

    if (field.contains("/")) {
        val step = field.split("/")
        val range = parseField(step[0], fieldDescriptor)
        val interval = step[1].toInt()
        return res + (range.indices step (interval)).map { range[it] }

    } else if (field.contains(",")) {
        return res + field.split(",")
            .map { it.toInt() }
            .onEach { validateIsInRange(it, fieldDescriptor) }

    } else if (field.contains("-")) {
        val range = field.split("-")
        val start = range[0].toInt()
        val end = range[1].toInt()

        val rng = min(start, end)..max(start, end)

        validateIsInRange(start, fieldDescriptor)
        validateIsInRange(end, fieldDescriptor)

        return res + rng
    } else if (field.contains("*")) {
        return res + (fieldDescriptor.minValue..fieldDescriptor.maxValue)
    } else {
        val toInt = field.toInt()
        validateIsInRange(toInt, fieldDescriptor)
        return res + toInt
    }
}

fun validateIsInRange(value: Int, fieldDescriptor: FieldDescriptor) {

    val validRange = fieldDescriptor.minValue..fieldDescriptor.maxValue

    if (value !in validRange) {
        println("${fieldDescriptor.desc.replaceFirstChar{it.uppercase()}} $value out of $validRange range")
        CronParser.exitWithError()
    }
}


