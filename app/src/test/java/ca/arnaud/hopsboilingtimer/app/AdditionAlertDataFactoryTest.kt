package ca.arnaud.hopsboilingtimer.app

import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertDataFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.BaseAdditionAlertData
import ca.arnaud.hopsboilingtimer.app.model.baseAdditionAlertDataDefault
import ca.arnaud.hopsboilingtimer.domain.fake.FakeTimeProvider
import ca.arnaud.hopsboilingtimer.domain.model.additionAlertEndDefault
import ca.arnaud.hopsboilingtimer.domain.model.additionScheduleDefault
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime

class AdditionAlertDataFactoryTest {

    private lateinit var subject: AdditionAlertDataFactory

    private val timeProvider = FakeTimeProvider()

    @Before
    fun setup() {
        subject = AdditionAlertDataFactory(
            timeProvider = timeProvider
        )
    }

    // region Next Alerts

    @Test
    fun `GIVEN schedule with empty alerts WHEN creating VERIFY empty next alerts`() {
        val alert = additionAlertEndDefault
        val schedule = additionScheduleDefault.copy(
            alerts = emptyList()
        )

        val result = subject.create(alert, schedule)
        assertEquals(emptyList<BaseAdditionAlertData>(), result.nextAlerts)
    }

    @Test
    fun `GIVEN invalid alert and schedule with invalid alerts WHEN creating VERIFY empty next alerts`() {
        val alert = additionAlertEndDefault
        val schedule = additionScheduleDefault.copy(
            alerts = listOf(
                additionAlertEndDefault, additionAlertEndDefault, additionAlertEndDefault
            )
        )

        val result = subject.create(alert, schedule)
        assertEquals(emptyList<BaseAdditionAlertData>(), result.nextAlerts)
    }

    @Test
    fun `GIVEN valid alert and schedule with invalid alerts WHEN creating VERIFY empty next alerts`() {
        val alert = additionAlertEndDefault.copy(id = "1")
        val schedule = additionScheduleDefault.copy(
            alerts = listOf(
                additionAlertEndDefault, additionAlertEndDefault, additionAlertEndDefault
            )
        )

        val result = subject.create(alert, schedule)
        assertEquals(emptyList<BaseAdditionAlertData>(), result.nextAlerts)
    }

    @Test
    fun `GIVEN valid alert and schedule with valid alerts without alert WHEN creating VERIFY empty next alerts`() {
        val alert = additionAlertEndDefault.copy(id = "1")
        val schedule = additionScheduleDefault.copy(
            alerts = listOf(
                additionAlertEndDefault.copy(id = "2"),
                additionAlertEndDefault.copy(id = "3"),
                additionAlertEndDefault.copy(id = "4")
            )
        )

        val result = subject.create(alert, schedule)
        assertEquals(emptyList<BaseAdditionAlertData>(), result.nextAlerts)
    }

    @Test
    fun `GIVEN schedule alerts and alert from the start WHEN creating VERIFY next alerts`() {
        val alert = additionAlertEndDefault.copy(id = "1")
        val schedule = additionScheduleDefault.copy(
            alerts = listOf(
                additionAlertEndDefault.copy(id = "1"),
                additionAlertEndDefault.copy(id = "2"),
                additionAlertEndDefault.copy(id = "3")
            )
        )

        val result = subject.create(alert, schedule)
        assertEquals(
            listOf(
                baseAdditionAlertDataDefault.copy(id = "2"),
                baseAdditionAlertDataDefault.copy(id = "3"),
            ).map { it.id },
            result.nextAlerts.map { it.id }
        )
    }

    @Test
    fun `GIVEN schedule alerts and alert from the middle WHEN creating VERIFY just end alert`() {
        val alert = additionAlertEndDefault.copy(id = "2")
        val schedule = additionScheduleDefault.copy(
            alerts = listOf(
                additionAlertEndDefault.copy(id = "1"),
                additionAlertEndDefault.copy(id = "2"),
                additionAlertEndDefault.copy(id = "3")
            )
        )

        val result = subject.create(alert, schedule)
        assertEquals(
            listOf(
                baseAdditionAlertDataDefault.copy(id = "3"),
            ).map { it.id },
            result.nextAlerts.map { it.id }
        )
    }

    @Test
    fun `GIVEN schedule alerts and alert from the end WHEN creating VERIFY empty next alerts`() {
        val alert = additionAlertEndDefault.copy(id = "3")
        val schedule = additionScheduleDefault.copy(
            alerts = listOf(
                additionAlertEndDefault.copy(id = "1"),
                additionAlertEndDefault.copy(id = "2"),
                additionAlertEndDefault.copy(id = "3")
            )
        )

        val result = subject.create(alert, schedule)
        assertEquals(
            emptyList<String>(),
            result.nextAlerts.map { it.id }
        )
    }

    // endregion

    // region Initial Delay

    @Test
    fun `GIVEN trigger at same time as now WHEN creating VERIFY zero initial delay`() {
        timeProvider.now = LocalDateTime.of(2023, 7, 19, 12, 0)
        val triggerAt = timeProvider.getNowLocalDateTime()
        val alert = additionAlertEndDefault.copy(triggerAtTime = triggerAt)
        val schedule = additionScheduleDefault

        val result = subject.create(alert, schedule)
        assertEquals(Duration.ZERO, result.initialDelay)
    }

    @Test
    fun `GIVEN trigger at before now WHEN creating VERIFY zero initial delay`() {
        timeProvider.now = LocalDateTime.of(2023, 7, 19, 12, 0)
        val delay = Duration.ofMinutes(1)
        val triggerAt = timeProvider.getNowLocalDateTime() - delay
        val alert = additionAlertEndDefault.copy(triggerAtTime = triggerAt)
        val schedule = additionScheduleDefault

        val result = subject.create(alert, schedule)
        assertEquals(Duration.ZERO, result.initialDelay)
    }

    @Test
    fun `GIVEN trigger at after now WHEN creating VERIFY correct initial delay`() {
        timeProvider.now = LocalDateTime.of(2023, 7, 19, 12, 0)
        val delay = Duration.ofMinutes(1)
        val triggerAt = timeProvider.getNowLocalDateTime() + delay
        val alert = additionAlertEndDefault.copy(triggerAtTime = triggerAt)
        val schedule = additionScheduleDefault

        val result = subject.create(alert, schedule)
        assertEquals(delay, result.initialDelay)
    }

    // endregion
}