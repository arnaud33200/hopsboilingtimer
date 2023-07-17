package ca.arnaud.hopsboilingtimer.app

import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Duration

class DurationTextFormatterTest {

    private lateinit var subject: DurationTextFormatter

    @Before
    fun setup() {
        subject = DurationTextFormatter()
    }

    @Test
    fun `GIVEN zero duration WHEN formatting VERIFY zero secs`() {
        val duration = Duration.ZERO
        assertEquals("0 sec", subject.format(duration))
    }

    @Test
    fun `GIVEN less than 1s duration WHEN formatting VERIFY zero secs`() {
        val duration = Duration.ofMillis(999)
        assertEquals("0 sec", subject.format(duration))
    }

    @Test
    fun `GIVEN less than 10s duration WHEN formatting VERIFY 1 digit`() {
        val duration = Duration.ofSeconds(9)
        assertEquals("9 sec", subject.format(duration))
    }

    @Test
    fun `GIVEN less than minute duration WHEN formatting VERIFY seconds format`() {
        val duration = Duration.ofSeconds(59)
        assertEquals("59 sec", subject.format(duration))
    }

    @Test
    fun `GIVEN negative duration WHEN formatting VERIFY negative secs`() {
        val duration = Duration.ofSeconds(-59)
        assertEquals("-59 sec", subject.format(duration))
    }

    @Test
    fun `GIVEN less than 10 minute duration WHEN formatting VERIFY 1 digit`() {
        val duration = Duration.ofMinutes(9)
        assertEquals("9 min", subject.format(duration))
    }

    @Test
    fun `GIVEN hours duration WHEN formatting VERIFY minute formatting`() {
        val duration = Duration.ofHours(3)
        assertEquals("180 min", subject.format(duration))
    }

    @Test
    fun `GIVEN negative hours duration WHEN formatting VERIFY negative minute formatting`() {
        val duration = Duration.ofHours(-3)
        assertEquals("-180 min", subject.format(duration))
    }
}