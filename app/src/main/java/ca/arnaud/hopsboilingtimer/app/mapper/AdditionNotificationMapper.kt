package ca.arnaud.hopsboilingtimer.app.mapper

import ca.arnaud.hopsboilingtimer.app.alarm.AdditionNotification
import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import java.time.Duration
import javax.inject.Inject

class AdditionNotificationMapper @Inject constructor() :
    DataMapper<AdditionAlert, AdditionNotification> {

    override fun mapTo(input: AdditionAlert): AdditionNotification {
        return when (input) {
            is AdditionAlert.Start -> mapStart(input)
            is AdditionAlert.Next -> mapNext(input)
            is AdditionAlert.End -> mapEnd(input)
        }
    }

    private fun mapStart(input: AdditionAlert.Start): AdditionNotification {
        val additions = input.additions
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return AdditionNotification(
            message = "Start$hops",
            triggerAtMillis = input.triggerAtTime
        )
    }

    private fun mapNext(input: AdditionAlert.Next): AdditionNotification {
        val additions = input.additions
        val duration = input.additions.firstOrNull()?.duration ?: Duration.ZERO
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return AdditionNotification(
            message = "Next Additions (${getRemainingTimeText(duration)})$hops",
            triggerAtMillis = input.triggerAtTime
        )
    }

    private fun mapEnd(input: AdditionAlert.End): AdditionNotification {
        return AdditionNotification(
            message = "Stop Boiling!",
            triggerAtMillis = input.triggerAtTime
        )
    }

    private fun getRemainingTimeText(countDown: Duration): String {
        return when {
            countDown.toMinutes() > 0 -> "${countDown.toMinutes()} min"
            else -> "${countDown.toMillis() / 1000} sec"
        }
    }
}