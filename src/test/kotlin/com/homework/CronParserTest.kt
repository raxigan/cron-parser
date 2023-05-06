package com.homework

import com.homework.CronParser.Companion.ERROR_CODE
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

        assertOutputEquals("Usage: cron_parser.kts <cron_expression>")
    }

    @ParameterizedTest
    @ValueSource(strings = ["-h", "--help"])
    fun `should print manual and exit when help argument provided`(arg: String) {

        tryParse(arrayOf(arg))

        assertOutputEquals("Usage: cron_parser.kts <cron_expression>")
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
            0 0 1 * 1 /usr/bin/find              | month         1 2 3 4 5 6 7 8 9 10 11 12
            0 0 1 1-12 1 /usr/bin/find           | month         1 2 3 4 5 6 7 8 9 10 11 12
            0 0 1 3-6 1 /usr/bin/find            | month         3 4 5 6
            0 0 1 1-12/2 1 /usr/bin/find         | month         1 3 5 7 9 11
            0 0 1 3-12/2 1 /usr/bin/find         | month         3 5 7 9 11
            0 0 1 1,3,5,7,9,11/2 1 /usr/bin/find | month         1 5 9"""
    )
    internal fun `should print hours output when valid input was provided`(input: String, expectedMonths: String) {

        tryParse(arrayOf(input))

        assertOutputEquals(expectedMonths, 3)
    }

    @Test
    internal fun `should print output when valid input was provided`() {

        tryParse(arrayOf("*/15 0 1,15 * 1-5 /usr/bin/find"))

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
    internal fun `should print output when valid input with asterisks was provided`() {

        tryParse(arrayOf("*/2 * * * * /usr/bin/find"))

        assertOutputEquals(
            """
                minute        0 2 4 6 8 10 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40 42 44 46 48 50 52 54 56 58
                hour          0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
                day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5 6 7
                command       /usr/bin/find
            """
        )
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        textBlock = """
            a 0 1,15 * * /usr/bin/find    | Invalid cron expression format: 'a'
            -1,2 0 1,15 * * /usr/bin/find | Invalid cron expression format: '-1,2'
            ,2 0 1,15 * * /usr/bin/find   | Invalid cron expression format: ',2'
            0 0/ 1,15 * * /usr/bin/find   | Invalid cron expression format: '0/'"""
    )
    internal fun `should print error message when invalid cron component was provided`(
        input: String,
        expectedMessage: String
    ) {

        tryParse(arrayOf(input))

        assertOutputEquals(expectedMessage)
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        textBlock = """
            1-60 0 1,15 * * /usr/bin/find   | Field 'minute' 1-60 out of 0..59 range
            1,2 24 1,15 * * /usr/bin/find   | Field 'hour' 24 out of 0..23 range
            1,2 1,27 1,15 * * /usr/bin/find | Field 'hour' 1,27 out of 0..23 range"""
    )
    internal fun `should print error message when out of range cron component was provided`(
        input: String,
        expectedMessage: String
    ) {

        tryParse(arrayOf(input))

        assertOutputEquals(expectedMessage)
    }

    @Test
    internal fun `should handle multiple spaces between cron components`() {

        tryParse(arrayOf("1  1 1,15  * * /usr/bin/find"))

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
            CronParser().parse(args)
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

    private fun assertOutputEquals(expected: String, lineNo: Int) {
        assertEquals(expected.trimIndent(), outputStreamCaptor.toString().split(System.lineSeparator())[lineNo])
    }
}

