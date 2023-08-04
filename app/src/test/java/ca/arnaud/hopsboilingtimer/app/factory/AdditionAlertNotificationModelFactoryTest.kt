package ca.arnaud.hopsboilingtimer.app.factory

import ca.arnaud.hopsboilingtimer.app.feature.alert.factory.AdditionAlertNotificationModelFactory
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.app.formatter.time.TimeHoursTextFormatter
import ca.arnaud.hopsboilingtimer.app.model.additionAlertDataDefault
import ca.arnaud.hopsboilingtimer.commontest.model.additionAlertEndDefault
import ca.arnaud.hopsboilingtimer.commontest.model.additionAlertNextDefault
import ca.arnaud.hopsboilingtimer.commontest.model.additionAlertStartDefault
import ca.arnaud.hopsboilingtimer.commontest.model.additionScheduleDefault
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AdditionAlertNotificationModelFactoryTest {

    private lateinit var subject: AdditionAlertNotificationModelFactory

    private val durationTextFormatter: DurationTextFormatter = mockk()
    private val timeHoursTextFormatter: TimeHoursTextFormatter = mockk()

    @Before
    fun setup() {
        every { durationTextFormatter.format(any()) } returns ""
        every { timeHoursTextFormatter.format(any()) } returns ""

        subject = AdditionAlertNotificationModelFactory(
            durationTextFormatter = durationTextFormatter,
            timeHoursTextFormatter = timeHoursTextFormatter,
        )
    }

    @Test
    fun `GIVEN current alert and schedule with next alerts WHEN creating VERIFY current alert excluded`() {
        val currentAlert = additionAlertDataDefault.copy(id = "2")
        val schedule = additionScheduleDefault.copy(
            alerts = listOf(
                additionAlertStartDefault.copy(id = "1"),
                additionAlertNextDefault.copy(id = "2"),
                additionAlertNextDefault.copy(id = "3"),
                additionAlertEndDefault.copy(id = "4"),
            )
        )

        val result = subject.create(currentAlert, schedule)
        assertEquals("3,4", result.rows.joinToString(separator = ",") { it.id })
    }
}