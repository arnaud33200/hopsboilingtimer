package ca.arnaud.hopsboilingtimer.domain.model

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration

class AdditionAlertKtTest {

    val defaultAlert = AdditionAlert(Duration.ZERO, emptyList())

    @Test
    fun `GIVEN empty list WHEN getMinInterval VERIFY zero`() {
        val alerts = emptyList<AdditionAlert>()
        assertEquals(Duration.ofMinutes(0), alerts.getMinInterval())
    }

    @Test
    fun `GIVEN one addition alert WHEN getMinInterval VERIFY min alert countdown`() {
        val alerts = listOf(
            defaultAlert.copy(countDown = Duration.ofMinutes(5))
        )

        assertEquals(Duration.ofMinutes(5), alerts.getMinInterval())
    }

    @Test
    fun `GIVEN two addition alerts with 15 min interval WHEN getMinInterval VERIFY 15 min interval`() {
        val alerts = listOf(
            defaultAlert.copy(countDown = Duration.ofMinutes(20)),
            defaultAlert.copy(countDown = Duration.ofMinutes(35))
        )

        assertEquals(Duration.ofMinutes(15), alerts.getMinInterval())
    }

    @Test
    fun `GIVEN not ordered additions alerts WHEN getMinInterval VERIFY min is correct`() {
        val alerts = listOf(
            defaultAlert.copy(countDown = Duration.ofMinutes(0)),
            defaultAlert.copy(countDown = Duration.ofMinutes(55)),
            defaultAlert.copy(countDown = Duration.ofMinutes(30)),
            defaultAlert.copy(countDown = Duration.ofMinutes(10)),
            defaultAlert.copy(countDown = Duration.ofMinutes(15)),
            defaultAlert.copy(countDown = Duration.ofMinutes(20)),
        )

        assertEquals(Duration.ofMinutes(5), alerts.getMinInterval())
    }

    @Test
    fun `GIVEN ordered additions alerts WHEN getMinInterval VERIFY min is correct`() {
        val alerts = listOf(
            defaultAlert.copy(countDown = Duration.ofMinutes(0)),
            defaultAlert.copy(countDown = Duration.ofMinutes(15)),
            defaultAlert.copy(countDown = Duration.ofMinutes(25)),
            defaultAlert.copy(countDown = Duration.ofMinutes(40)),
            defaultAlert.copy(countDown = Duration.ofMinutes(55)),
        )

        assertEquals(Duration.ofMinutes(10), alerts.getMinInterval())
    }

    @Test
    fun `GIVEN addition alert with same countdown WHEN getMinInterval VERIFY zero`() {
        val alerts = listOf(
            defaultAlert.copy(countDown = Duration.ofMinutes(15)),
            defaultAlert.copy(countDown = Duration.ofMinutes(15)),
            defaultAlert.copy(countDown = Duration.ofMinutes(15)),
            defaultAlert.copy(countDown = Duration.ofMinutes(15)),
            defaultAlert.copy(countDown = Duration.ofMinutes(15)),
        )

        assertEquals(Duration.ofMinutes(0), alerts.getMinInterval())
    }
}