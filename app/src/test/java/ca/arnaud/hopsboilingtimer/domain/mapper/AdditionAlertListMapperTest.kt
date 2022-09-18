package ca.arnaud.hopsboilingtimer.domain.mapper

import ca.arnaud.hopsboilingtimer.domain.fake.FakeTimeProvider
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Duration

class AdditionAlertListMapperTest {

    lateinit var subject: AdditionAlertListMapper

    private val timeProvider = FakeTimeProvider()

    private val defaultHopsAddition = Addition(
        name = "",
        duration = Duration.ZERO
    )

    @Before
    fun setup() {
        timeProvider.nowMillis = 0L

        subject = AdditionAlertListMapper(
            timeProvider
        )
    }

    @Test
    fun `GIVEN empty list WHEN execution VERIFY empty list`() {
        val input = emptyList<Addition>()
        val result = subject.mapTo(input)
        assert(result.isEmpty())
    }

    @Test
    fun `GIVEN zero duration list WHEN execution VERIFY empty list`() {
        val input = listOf(
            defaultHopsAddition.copy(duration = Duration.ZERO),
        )

        val result = subject.mapTo(input)
        val countdowns = result.map { it.toCountdown(timeProvider) }
        assertEquals(listOf(0).toMinutes(), countdowns)
    }

    @Test
    fun `GIVEN one hopsAddition WHEN execution VERIFY empty list`() {
        val input = listOf(
            defaultHopsAddition.copy(duration = Duration.ofMinutes(60)),
        )

        val result = subject.mapTo(input)
        val countdowns = result.map { it.toCountdown(timeProvider) }
        assertEquals(listOf(0).toMinutes(), countdowns)
    }

    @Test
    fun `GIVEN ordered duration WHEN execution VERIFY correct out put`() {
        val input = listOf(
            defaultHopsAddition.copy(duration = Duration.ofMinutes(60)),
            defaultHopsAddition.copy(duration = Duration.ofMinutes(45)),
            defaultHopsAddition.copy(duration = Duration.ofMinutes(20)),
            defaultHopsAddition.copy(duration = Duration.ofMinutes(5))
        )

        val result = subject.mapTo(input)
        val countdowns = result.map { it.toCountdown(timeProvider) }
        assertEquals(listOf(0, 15, 40, 55).toMinutes(), countdowns)
    }

    @Test
    fun `GIVEN not ordered duration WHEN execution VERIFY correct out put`() {
        val addition60Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(60))
        val addition45Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(45))
        val addition20Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(20))
        val addition5Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(5))

        val input = listOf(
            addition5Min, addition20Min, addition45Min, addition60Min
        )

        val result = subject.mapTo(input)
        val expected = listOf(
            AdditionAlert(Duration.ofMinutes(0).toMillis(), listOf(addition60Min)),
            AdditionAlert(Duration.ofMinutes(15).toMillis(), listOf(addition45Min)),
            AdditionAlert(Duration.ofMinutes(40).toMillis(), listOf(addition20Min)),
            AdditionAlert(Duration.ofMinutes(55).toMillis(), listOf(addition5Min)),
        )
        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN additions with duplicate duration WHEN mapping VERIFY correct out put`() {
        val addition60Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(60))
        val addition45Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(45))
        val addition20Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(20))
        val addition20Min2 = defaultHopsAddition.copy(duration = Duration.ofMinutes(20))
        val addition5Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(5))
        val addition5Min2 = defaultHopsAddition.copy(duration = Duration.ofMinutes(5))

        val input = listOf(
            addition20Min, addition5Min, addition60Min, addition20Min2, addition45Min, addition5Min2
        )

        val result = subject.mapTo(input)
        val expected = listOf(
            AdditionAlert(Duration.ofMinutes(0).toMillis(), listOf(addition60Min)),
            AdditionAlert(Duration.ofMinutes(15).toMillis(), listOf(addition45Min)),
            AdditionAlert(Duration.ofMinutes(40).toMillis(), listOf(addition20Min, addition20Min2)),
            AdditionAlert(Duration.ofMinutes(55).toMillis(), listOf(addition5Min, addition5Min2)),
        )
        assertEquals(expected, result)
    }
}

private fun AdditionAlert.toCountdown(timeProvider: TimeProvider): Duration {
    return Duration.ofMillis(triggerAtTime - timeProvider.getNowTimeMillis())
}

private fun List<Int>.toMinutes(): List<Duration> {
    return map { Duration.ofMinutes(it.toLong()) }
}
