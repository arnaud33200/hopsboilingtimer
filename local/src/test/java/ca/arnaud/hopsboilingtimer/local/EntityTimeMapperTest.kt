package ca.arnaud.hopsboilingtimer.local

import ca.arnaud.hopsboilingtimer.commontest.fake.FakeTimeProvider
import ca.arnaud.hopsboilingtimer.local.mapper.EntityTimeMapper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId

class EntityTimeMapperTest {

    private lateinit var subject: EntityTimeMapper

    private val timeProvider = FakeTimeProvider()

    @Before
    fun setup() {
        subject = EntityTimeMapper(
            timeProvider = timeProvider
        )
    }

    @Test
    fun `GIVEN min local date time WHEN mapping to VERIFY zero`() {
        val localDateTime = LocalDateTime.MIN
        assertEquals(0L, subject.mapTo(localDateTime))
    }

    @Test
    fun `GIVEN max local date time WHEN mapping to VERIFY zero`() {
        val localDateTime = LocalDateTime.MAX
        assertEquals(0L, subject.mapTo(localDateTime))
    }

    @Test
    fun `GIVEN zero timestamp WHEN mapping from VERIFY beginning of time`() {
        val timestamp = 0L
        assertEquals(LocalDateTime.of(1970, 1, 1, 0, 0, 0), subject.mapFrom(timestamp))
    }

    @Test
    fun `GIVEN toronto local date time WHEN mapping to VERIFY utc timestamp`() {
        val localDateTime = LocalDateTime.of(2023, 7, 17, 23, 30, 0)
        timeProvider.zoneId = ZoneId.of("America/Toronto")
        assertEquals(
            1689651000000L,
            subject.mapTo(localDateTime)
        )
    }

    @Test
    fun `GIVEN epoch timestamp WHEN mapping from VERIFY toronto local date time `() {
        val epochTimestamp = 1689651000000L
        timeProvider.zoneId = ZoneId.of("America/Toronto")
        assertEquals(
            LocalDateTime.of(2023, 7, 17, 23, 30, 0),
            subject.mapFrom(epochTimestamp)
        )
    }

    @Test
    fun `GIVEN paris local date time WHEN mapping to VERIFY utc timestamp`() {
        val localDateTime = LocalDateTime.of(2023, 7, 17, 0, 30, 0)
        timeProvider.zoneId = ZoneId.of("Europe/Paris")
        assertEquals(
            1689546600000L,
            subject.mapTo(localDateTime)
        )
    }

    @Test
    fun `GIVEN epoch timestamp WHEN mapping from VERIFY paris local date time `() {
        val epochTimestamp = 1689546600000L
        timeProvider.zoneId = ZoneId.of("Europe/Paris")
        assertEquals(
            LocalDateTime.of(2023, 7, 17, 0, 30, 0),
            subject.mapFrom(epochTimestamp)
        )
    }
}