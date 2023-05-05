package com.homework

import com.homework.CronParser.ERROR_CODE
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@ExtendWith(MockKExtension::class)
class CronParserTest {

    private val standardOut = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
        mockkStatic("com.homework.CronParserKt")
        every { exitWithError() } throws RuntimeException(ERROR_CODE.toString())
    }

    @AfterEach
    fun tearDown() {
        System.setOut(standardOut)
    }

    @Test
    fun `should print manual and exit when no arguments provided`() {

        tryParse(emptyArray())

        assertOutputEquals("Usage: cron_parser.kts <cron_string>")
    }

    @ParameterizedTest
    @ValueSource(strings = ["-h", "--help"])
    fun `should print manual and exit when help argument provided`(arg: String) {

        tryParse(arrayOf(arg))

        assertOutputEquals("Usage: cron_parser.kts <cron_string>")
    }

    @Test
    internal fun `should print error message when too few arguments provided`() {

        tryParse(arrayOf("1 1 1 1 1"))

        assertOutputEquals("Too few cron components provided. Please provide a valid cron string.")
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        textBlock = """
            0 0 1 * 1 /usr/bin/find                | month         1 2 3 4 5 6 7 8 9 10 11 12
            0 0 1 1-12 1 /usr/bin/find             | month         1 2 3 4 5 6 7 8 9 10 11 12
            0 0 1 1-12/2 1 /usr/bin/find           | month         1 3 5 7 9 11
            0 0 1 1,3,5,7,9,11/2 1 /usr/bin/find   | month         1 5 9"""
    )
    internal fun `should print output when valid input was provided`(args: String, expectedMonths: String) {

        tryParse(arrayOf(args))

        assertEquals(expectedMonths, outputStreamCaptor.toString().split(System.lineSeparator())[3])
    }

    @Test
    internal fun `should print output when valid input was provided`() {

        parse(arrayOf("*/15 0 1,15 * 1-5 /usr/bin/find"))

        assertOutputEquals(
            """
                minute        0 15 30 45
                hour          0
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5
                command       /usr/bin/find
            """
        )
    }

    @Test
    internal fun `should print output when valid input was provided 2`() {

        parse(arrayOf("1-20/2 0 1,15 * 1-5 /usr/bin/find"))

        assertOutputEquals(
            """
                minute        1 3 5 7 9 11 13 15 17 19
                hour          0
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5
                command       /usr/bin/find
            """
        )
    }

    @Test
    internal fun `should print output when valid input was provided 3`() {

        parse(arrayOf("*/15 0 1,15 * * /usr/bin/find"))

        assertOutputEquals(
            """
                minute        0 15 30 45
                hour          0
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5 6 7
                command       /usr/bin/find
            """
        )
    }

    @Test
    internal fun `should print output when valid input was provided 4`() {

        parse(arrayOf("1,2,3,4,5,6,7,8/2 0 1,15 * * /usr/bin/find"))

        assertOutputEquals(
            """
                minute        1 3 5 7
                hour          0
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5 6 7
                command       /usr/bin/find
            """
        )
    }

    @Test
    internal fun `should print error message when invalid cron component was provided`() {

        tryParse(arrayOf("a 0 1,15 * * /usr/bin/find"))

        assertOutputEquals("Invalid cron format on component 'a'")
    }

    @Test
    internal fun `should print error message when out of range cron component was provided`() {

        tryParse(arrayOf("1-60 0 1,15 * * /usr/bin/find"))

        assertOutputEquals("Field 'minute' 60 out of 0..59 range")
    }

    @Test
    internal fun `should print error message when out of range cron component was provided 2`() {

        tryParse(arrayOf("1,2 24 1,15 * * /usr/bin/find"))

        assertOutputEquals("Field 'hour' 24 out of 0..23 range")
    }

    @Test
    internal fun `should print error message when out of range cron component was provided 3`() {

        tryParse(arrayOf("1,2 1,27 1,15 * * /usr/bin/find"))

        assertOutputEquals("Field 'hour' 27 out of 0..23 range")
    }

    @Test
    internal fun `should print error message when negative cron component was provided`() {

        tryParse(arrayOf("-1,2 0 1,15 * * /usr/bin/find"))

        assertOutputEquals("Invalid cron format on component '-1,2'")
    }

    @Test
    internal fun `should handle multiple spaces between cron components`() {

        tryParse(arrayOf("1  1 1,15 * * /usr/bin/find"))

        assertOutputEquals(
            """
                minute        1
                hour          1
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5 6 7
                command       /usr/bin/find
            """
        )
    }

    private fun tryParse(args: Array<String>): RuntimeException? {

        return try {
            parse(args)
            null
        } catch (ex: RuntimeException) {
            System.setOut(standardOut)
            println(outputStreamCaptor.toString())
            ex.printStackTrace()
            assertEquals(1, ex.message?.toInt())
            ex
        }
    }

    private fun assertOutputEquals(expected: String) {
        assertEquals(expected.trimIndent() + System.lineSeparator(), outputStreamCaptor.toString())
    }
}

