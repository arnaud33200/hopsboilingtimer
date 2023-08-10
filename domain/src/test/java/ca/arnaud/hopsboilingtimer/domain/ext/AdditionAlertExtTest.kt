package ca.arnaud.hopsboilingtimer.domain.ext

import ca.arnaud.hopsboilingtimer.commontest.model.additionAlertEndDefault
import ca.arnaud.hopsboilingtimer.commontest.model.additionScheduleDefault
import ca.arnaud.hopsboilingtimer.domain.extension.getNextAlerts
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import org.junit.Assert.assertEquals
import org.junit.Test

class AdditionAlertExtTest {

    // region Next Alerts

    @Test
    fun `GIVEN schedule with empty alerts WHEN creating VERIFY empty next alerts`() {
        val alert = additionAlertEndDefault
        val schedule = additionScheduleDefault.copy(
            alerts = emptyList()
        )

        val result = alert.getNextAlerts(schedule)
        assertEquals(emptyList<AdditionAlert>(), result)
    }

    @Test
    fun `GIVEN invalid alert and schedule with invalid alerts WHEN creating VERIFY empty next alerts`() {
        val alert = additionAlertEndDefault
        val schedule = additionScheduleDefault.copy(
            alerts = listOf(
                additionAlertEndDefault, additionAlertEndDefault, additionAlertEndDefault
            )
        )

        val result = alert.getNextAlerts(schedule)
        assertEquals(emptyList<AdditionAlert>(), result)
    }

    @Test
    fun `GIVEN valid alert and schedule with invalid alerts WHEN creating VERIFY empty next alerts`() {
        val alert = additionAlertEndDefault.copy(id = "1")
        val schedule = additionScheduleDefault.copy(
            alerts = listOf(
                additionAlertEndDefault, additionAlertEndDefault, additionAlertEndDefault
            )
        )

        val result = alert.getNextAlerts(schedule)
        assertEquals(emptyList<AdditionAlert>(), result)
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

        val result = alert.getNextAlerts(schedule)
        assertEquals(emptyList<AdditionAlert>(), result)
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

        val result = alert.getNextAlerts(schedule)
        assertEquals(
            listOf(
                additionAlertEndDefault.copy(id = "2"),
                additionAlertEndDefault.copy(id = "3"),
            ).map { it.id },
            result.map { it.id }
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

        val result = alert.getNextAlerts(schedule)
        assertEquals(
            listOf(
                additionAlertEndDefault.copy(id = "3"),
            ).map { it.id },
            result.map { it.id }
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

        val result = alert.getNextAlerts(schedule)
        assertEquals(
            emptyList<String>(),
            result.map { it.id }
        )
    }

    // endregion
}