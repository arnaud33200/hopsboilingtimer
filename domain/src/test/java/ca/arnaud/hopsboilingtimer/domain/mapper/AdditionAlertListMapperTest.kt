package ca.arnaud.hopsboilingtimer.domain.mapper

import ca.arnaud.hopsboilingtimer.domain.fake.FakeTimeProvider
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.additionAlertEndDefault
import ca.arnaud.hopsboilingtimer.domain.model.additionAlertNextDefault
import ca.arnaud.hopsboilingtimer.domain.model.additionAlertStartDefault
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime

class AdditionAlertListMapperTest {

    lateinit var subject: AdditionAlertListMapper

    private val timeProvider = FakeTimeProvider()
    private val idFactory: IdFactory = mockk()

    private val defaultHopsAddition = Addition(
        name = "",
        duration = Duration.ZERO
    )

    @Before
    fun setup() {
        timeProvider.now = LocalDateTime.MIN

        every { idFactory.createRandomId() } returns ""

        subject = AdditionAlertListMapper(
            timeProvider = timeProvider,
            idFactory = idFactory,
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
            additionAlertStartDefault.copy(
                triggerAtTime = 0L.minutesToTriggerAt(timeProvider),
                additions = listOf(zeroAddition),
            ),
            additionAlertEndDefault.copy(triggerAtTime = 0L.minutesToTriggerAt(timeProvider)),
        )
        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN one hopsAddition WHEN execution VERIFY empty list`() {
        val addition60Min = defaultHopsAddition.copy(duration = Duration.ofMinutes(60))
        val input = listOf(addition60Min)

        val result = subject.mapTo(input)

        val expected = listOf(
            additionAlertStartDefault.copy(
                triggerAtTime = timeProvider.now + Duration.ofMinutes(0),
                additions = listOf(addition60Min),
            ),
            additionAlertEndDefault.copy(
                triggerAtTime = timeProvider.now + Duration.ofMinutes(60),
                duration = Duration.ofMinutes(60),
            ),
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
            additionAlertStartDefault.copy(
                triggerAtTime = 0L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition60Min),
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 15L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition45Min),
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 40L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition20Min),
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 55L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition5Min),
            ),
            additionAlertEndDefault.copy(
                triggerAtTime = 60L.minutesToTriggerAt(timeProvider),
                duration = Duration.ofMinutes(60),
            ),
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
            additionAlertStartDefault.copy(
                triggerAtTime = 0L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition60Min),
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 15L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition45Min)
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 40L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition20Min)
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 55L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition5Min)
            ),
            additionAlertEndDefault.copy(
                triggerAtTime = 60L.minutesToTriggerAt(timeProvider),
                duration = Duration.ofMinutes(60),
            ),
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
            additionAlertStartDefault.copy(
                triggerAtTime = 0L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition60Min),
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 15L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition45Min)
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 40L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition20Min)
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 55L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition5Min)
            ),
            additionAlertEndDefault.copy(
                triggerAtTime = 60L.minutesToTriggerAt(timeProvider),
                duration = Duration.ofMinutes(60),
            ),
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
            additionAlertStartDefault.copy(
                triggerAtTime = 0L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition60Min),
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 15L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition45Min)
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 40L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition20Min, addition20Min2),
            ),
            additionAlertNextDefault.copy(
                triggerAtTime = 55L.minutesToTriggerAt(timeProvider),
                additions = listOf(addition5Min, addition5Min2),
            ),
            additionAlertEndDefault.copy(
                triggerAtTime = 60L.minutesToTriggerAt(timeProvider),
                duration = Duration.ofMinutes(60),
            ),
        )
        assertEquals(expected, result)
    }
}

private fun Long.minutesToTriggerAt(timeProvider: TimeProvider): LocalDateTime {
    return timeProvider.getNowLocalDateTime() + Duration.ofMinutes(this)
}

private fun AdditionAlert.toCountdown(timeProvider: TimeProvider): Duration {
    return Duration.between(triggerAtTime, timeProvider.getNowLocalDateTime())
}

private fun List<Int>.toMinutes(): List<Duration> {
    return map { Duration.ofMinutes(it.toLong()) }
}
