import CronParser.ERROR_CODE
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import java.io.ByteArrayOutputStream
import java.io.PrintStream

@ExtendWith(MockKExtension::class)
class CronParserTest {

    private val standardOut = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
        mockkObject(CronParser)
        every { CronParser.exitWithError() } throws RuntimeException(ERROR_CODE.toString())
    }

    @AfterEach
    fun tearDown() {
        System.setOut(standardOut)
    }

    @Test
    internal fun `should print manual and exit when no arguments provided`() {

        val exception: Exception = Assertions.assertThrows(RuntimeException::class.java) {
            parse(emptyArray())
        }

        assertEquals("Usage: kotlin CronParserKt <cron_string>\n", outputStreamCaptor.toString())
        assertEquals(1, exception.message?.toInt())
    }

    @Test
    internal fun `should print error message when too few arguments provided`() {

        val exception: Exception = Assertions.assertThrows(RuntimeException::class.java) {
            parse(arrayOf("1"))
        }

        assertEquals(
            "Too few cron components provided. Please provide a valid cron string.\n",
            outputStreamCaptor.toString()
        )
        assertEquals(1, exception.message?.toInt())
    }

    @Test
    internal fun `should print output when valid input was provided`() {

        parse(arrayOf("*/15 0 1,15 * 1-5 /usr/bin/find"))

        assertEquals(
            """
                minute        0 15 30 45
                hour          0
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5
                command       /usr/bin/find
                
            """.trimIndent(),
            outputStreamCaptor.toString()
        )
    }

    @Test
    internal fun `should print output when valid input was provided 2`() {

        parse(arrayOf("1-20/2 0 1,15 * 1-5 /usr/bin/find"))

        assertEquals(
            """
                minute        1 3 5 7 9 11 13 15 17 19
                hour          0
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5
                command       /usr/bin/find
                
            """.trimIndent(),
            outputStreamCaptor.toString()
        )
    }

    @Test
    internal fun `should print output when valid input was provided 3`() {

        parse(arrayOf("*/15 0 1,15 * * /usr/bin/find"))

        assertEquals(
            """
                minute        0 15 30 45
                hour          0
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5 6 7
                command       /usr/bin/find
                
            """.trimIndent(),
            outputStreamCaptor.toString()
        )
    }

    @Test
    internal fun `should print output when valid input was provided 4`() {

        parse(arrayOf("1,2,3,4,5,6,7,8/2 0 1,15 * * /usr/bin/find"))

        assertEquals(
            """
                minute        1 3 5 7
                hour          0
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5 6 7
                command       /usr/bin/find
                
            """.trimIndent(),
            outputStreamCaptor.toString()
        )
    }

    @Test
    internal fun `should print error message when invalid cron component was provided`() {

        val exception: Exception = Assertions.assertThrows(RuntimeException::class.java) {
            parse(arrayOf("a 0 1,15 * * /usr/bin/find"))
        }

        assertEquals(1, exception.message?.toInt())
        assertEquals(
            """
                Invalid cron format on component "a"
                
            """.trimIndent(),
            outputStreamCaptor.toString()
        )
    }

    @Test
    internal fun `should print error message when out of range cron component was provided`() {

        val exception: Exception = Assertions.assertThrows(RuntimeException::class.java) {
            parse(arrayOf("1-60 0 1,15 * * /usr/bin/find"))
        }

        assertEquals(1, exception.message?.toInt())
        assertEquals(
            """
                Minute 60 out of 0..59 range
                
            """.trimIndent(),
            outputStreamCaptor.toString()
        )
    }

    @Test
    internal fun `should print error message when out of range cron component was provided 2`() {

        val exception: Exception = Assertions.assertThrows(RuntimeException::class.java) {
            parse(arrayOf("1,2 24 1,15 * * /usr/bin/find"))
        }

        assertEquals(1, exception.message?.toInt())
        assertEquals(
            """
                Hour 24 out of 0..23 range
                
            """.trimIndent(),
            outputStreamCaptor.toString()
        )
    }

    @Test
    internal fun `should print error message when out of range cron component was provided 3`() {

        val exception: Exception = Assertions.assertThrows(RuntimeException::class.java) {
            parse(arrayOf("1,2 1,27 1,15 * * /usr/bin/find"))
        }

        assertEquals(1, exception.message?.toInt())
        assertEquals(
            """
                Hour 27 out of 0..23 range
                
            """.trimIndent(),
            outputStreamCaptor.toString()
        )
    }

    @Test
    internal fun `should print error message when negative cron component was provided`() {

        val exception: Exception = Assertions.assertThrows(RuntimeException::class.java) {
            parse(arrayOf("-1,2 0 1,15 * * /usr/bin/find"))
        }

        assertEquals(1, exception.message?.toInt())
        assertEquals(
            """
                Invalid cron format on component "-1,2"
                
            """.trimIndent(),
            outputStreamCaptor.toString()
        )
    }

    @Test
    internal fun `should handle multiple spaces between cron components`() {

        parse(arrayOf("1  1 1,15 * * /usr/bin/find"))

        assertEquals(
            """
                minute        1
                hour          1
                day of month  1 15
                month         1 2 3 4 5 6 7 8 9 10 11 12
                day of week   1 2 3 4 5 6 7
                command       /usr/bin/find
                
            """.trimIndent(),
            outputStreamCaptor.toString()
        )
    }
}
