package ca.arnaud.hopsboilingtimer.app

import ca.arnaud.hopsboilingtimer.app.formatter.time.RemainingTimeTextFormatter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Duration

class RemainingTimeTextFormatterTest {

    private lateinit var subject: RemainingTimeTextFormatter

    @Before
    fun setup() {
        subject = RemainingTimeTextFormatter()
    }

    @Test
    fun `GIVEN zero duration WHEN formatting VERIFY zero remaining time`() {
        val duration = Duration.ZERO
        assertEquals("00:00", subject.format(duration))
    }

    @Test
    fun `GIVEN less second duration WHEN formatting VERIFY zero remaining time`() {
        val duration = Duration.ofMillis(999)
        assertEquals("00:00", subject.format(duration))
    }

    @Test
    fun `GIVEN less 10 sec WHEN formatting VERIFY 2 digit sec`() {
        val duration = Duration.ofSeconds(9)
        assertEquals("00:09", subject.format(duration))
    }

    @Test
    fun `GIVEN negative secs WHEN formatting VERIFY negative`() {
        val duration = Duration.ofSeconds(-59)
        assertEquals("-00:59", subject.format(duration))
    }

    @Test
    fun `GIVEN exact minutes WHEN formatting VERIFY double zero digit for secs`() {
        val duration = Duration.ofMinutes(3)
        assertEquals("03:00", subject.format(duration))
    }

    @Test
    fun `GIVEN minutes and seconds WHEN formatting VERIFY both displayed`() {
        val duration = Duration.ofMinutes(9) + Duration.ofSeconds(9)
        assertEquals("09:09", subject.format(duration))
    }

    @Test
    fun `GIVEN more than 99 minutes WHEN formatting VERIFY 3 digits`() {
        val duration = Duration.ofMinutes(111) + Duration.ofSeconds(59)
        assertEquals("111:59", subject.format(duration))
    }

    @Test
    fun `GIVEN negative minutes and seconds WHEN formatting VERIFY negative format`() {
        val duration = Duration.ofSeconds(-666)
        assertEquals("-11:06", subject.format(duration))
    }
}