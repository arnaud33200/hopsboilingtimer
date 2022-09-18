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
        val zeroAddition = defaultHopsAddition.copy(duration = Duration.ZERO)
        val input = listOf(zeroAddition)

        val result = subject.mapTo(input)

        val expected = listOf(
            AdditionAlert.Start(0L.minutesToTriggerAt(timeProvider), listOf(zeroAddition)),
            AdditionAlert.End(0L.minutesToTriggerAt(timeProvider)),
        )
        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN one hopsAddition WHEN execution VERIFY empty list`() {
        val addition60Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(60))
        val input = listOf(addition60Min)

        val result = subject.mapTo(input)

        val expected = listOf(
            AdditionAlert.Start(Duration.ofMinutes(0).toMillis(), listOf(addition60Min)),
            AdditionAlert.End(Duration.ofMinutes(60).toMillis()),
        )
        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN ordered ascending duration WHEN execution VERIFY correct out put`() {
        val addition60Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(60))
        val addition45Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(45))
        val addition20Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(20))
        val addition5Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(5))

        val input = listOf(
            addition5Min, addition20Min, addition45Min, addition60Min
        )

        val result = subject.mapTo(input)
        val expected = listOf(
            AdditionAlert.Start(0L.minutesToTriggerAt(timeProvider), listOf(addition60Min)),
            AdditionAlert.Next(15L.minutesToTriggerAt(timeProvider), listOf(addition45Min)),
            AdditionAlert.Next(40L.minutesToTriggerAt(timeProvider), listOf(addition20Min)),
            AdditionAlert.Next(55L.minutesToTriggerAt(timeProvider), listOf(addition5Min)),
            AdditionAlert.End(60L.minutesToTriggerAt(timeProvider)),
        )
        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN ordered descending duration WHEN execution VERIFY correct out put`() {
        val addition60Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(60))
        val addition45Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(45))
        val addition20Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(20))
        val addition5Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(5))

        val input = listOf(
            addition60Min, addition45Min, addition20Min, addition5Min
        )

        val result = subject.mapTo(input)
        val expected = listOf(
            AdditionAlert.Start(0L.minutesToTriggerAt(timeProvider), listOf(addition60Min)),
            AdditionAlert.Next(15L.minutesToTriggerAt(timeProvider), listOf(addition45Min)),
            AdditionAlert.Next(40L.minutesToTriggerAt(timeProvider), listOf(addition20Min)),
            AdditionAlert.Next(55L.minutesToTriggerAt(timeProvider), listOf(addition5Min)),
            AdditionAlert.End(60L.minutesToTriggerAt(timeProvider)),
        )
        assertEquals(expected, result)
    }


    @Test
    fun `GIVEN not ordered duration WHEN execution VERIFY correct out put`() {
        val addition60Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(60))
        val addition45Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(45))
        val addition20Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(20))
        val addition5Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(5))

        val input = listOf(
            addition5Min, addition60Min, addition20Min, addition45Min
        )

        val result = subject.mapTo(input)
        val expected = listOf(
            AdditionAlert.Start(0L.minutesToTriggerAt(timeProvider), listOf(addition60Min)),
            AdditionAlert.Next(15L.minutesToTriggerAt(timeProvider), listOf(addition45Min)),
            AdditionAlert.Next(40L.minutesToTriggerAt(timeProvider), listOf(addition20Min)),
            AdditionAlert.Next(55L.minutesToTriggerAt(timeProvider), listOf(addition5Min)),
            AdditionAlert.End(60L.minutesToTriggerAt(timeProvider)),
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
            AdditionAlert.Start(0L.minutesToTriggerAt(timeProvider), listOf(addition60Min)),
            AdditionAlert.Next(15L.minutesToTriggerAt(timeProvider), listOf(addition45Min)),
            AdditionAlert.Next(
                40L.minutesToTriggerAt(timeProvider), listOf(addition20Min, addition20Min2)
            ),
            AdditionAlert.Next(
                55L.minutesToTriggerAt(timeProvider), listOf(addition5Min, addition5Min2)
            ),
            AdditionAlert.End(60L.minutesToTriggerAt(timeProvider)),
        )
        assertEquals(expected, result)
    }
}

private fun Long.minutesToTriggerAt(timeProvider: TimeProvider): Long {
    return timeProvider.getNowTimeMillis() + Duration.ofMinutes(this).toMillis()
}

private fun AdditionAlert.toCountdown(timeProvider: TimeProvider): Duration {
    return Duration.ofMillis(triggerAtTime - timeProvider.getNowTimeMillis())
}

private fun List<Int>.toMinutes(): List<Duration> {
    return map { Duration.ofMinutes(it.toLong()) }
}
