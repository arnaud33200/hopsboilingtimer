package ca.arnaud.hopsboilingtimer.app

import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertDataFactory
import ca.arnaud.hopsboilingtimer.domain.fake.FakeTimeProvider
import ca.arnaud.hopsboilingtimer.domain.model.additionAlertEndDefault
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime

class AdditionAlertDataFactoryTest {

    private lateinit var subject: AdditionAlertDataFactory

    private val timeProvider = ca.arnaud.hopsboilingtimer.domain.fake.FakeTimeProvider()

    @Before
    fun setup() {
        subject = AdditionAlertDataFactory(
            timeProvider = timeProvider
        )
    }

    // region Initial Delay

    @Test
    fun `GIVEN trigger at same time as now WHEN creating VERIFY zero initial delay`() {
        timeProvider.now = LocalDateTime.of(2023, 7, 19, 12, 0)
        val triggerAt = timeProvider.getNowLocalDateTime()
        val alert = ca.arnaud.hopsboilingtimer.domain.model.additionAlertEndDefault.copy(triggerAtTime = triggerAt)

        val result = subject.create(alert)
        assertEquals(Duration.ZERO, result.initialDelay)
    }

    @Test
    fun `GIVEN trigger at before now WHEN creating VERIFY zero initial delay`() {
        timeProvider.now = LocalDateTime.of(2023, 7, 19, 12, 0)
        val delay = Duration.ofMinutes(1)
        val triggerAt = timeProvider.getNowLocalDateTime() - delay
        val alert = ca.arnaud.hopsboilingtimer.domain.model.additionAlertEndDefault.copy(triggerAtTime = triggerAt)

        val result = subject.create(alert)
        assertEquals(Duration.ZERO, result.initialDelay)
    }

    @Test
    fun `GIVEN trigger at after now WHEN creating VERIFY correct initial delay`() {
        timeProvider.now = LocalDateTime.of(2023, 7, 19, 12, 0)
        val delay = Duration.ofMinutes(1)
        val triggerAt = timeProvider.getNowLocalDateTime() + delay
        val alert = ca.arnaud.hopsboilingtimer.domain.model.additionAlertEndDefault.copy(triggerAtTime = triggerAt)

        val result = subject.create(alert)
        assertEquals(delay, result.initialDelay)
    }

    // endregion
}