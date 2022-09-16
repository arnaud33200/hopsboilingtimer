package ca.arnaud.hopsboilingtimer.app.mapper

import ca.arnaud.hopsboilingtimer.app.alarm.AdditionNotification
import ca.arnaud.hopsboilingtimer.app.provider.TimeProviderImpl
import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import java.time.Duration
import javax.inject.Inject

class AdditionNotificationMapper @Inject constructor(
    private val timeProvider: TimeProviderImpl,
) : DataMapper<AdditionAlert, AdditionNotification> {

    override fun mapTo(input: AdditionAlert): AdditionNotification {
        val hops = input.additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return AdditionNotification(
            message = "${getRemainingTimeText(input.countDown)} Addition$hops",
            triggerAtMillis = timeProvider.getNowTimeMillis() + input.countDown.toMillis()
        )
    }

    private fun getRemainingTimeText(countDown: Duration): String {
        return when {
            countDown.toMinutes() > 0 -> "${countDown.toMinutes()} min"
            else -> "${countDown.toMillis() / 1000} sec"
        }
    }
}