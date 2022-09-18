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
            is AdditionAlert.Start -> createWith(input.additions, input.triggerAtTime)
            is AdditionAlert.Next -> createWith(input.additions, input.triggerAtTime)
            is AdditionAlert.End -> mapEnd(input)
        }
    }

    private fun createWith(additions: List<Addition>, triggerAt: Long): AdditionNotification {
        val duration = additions.firstOrNull()?.duration ?: Duration.ZERO
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return AdditionNotification(
            message = "${getRemainingTimeText(duration)} Addition$hops",
            triggerAtMillis = triggerAt
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