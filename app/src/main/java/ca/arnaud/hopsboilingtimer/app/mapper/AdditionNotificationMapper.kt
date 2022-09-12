package ca.arnaud.hopsboilingtimer.app.mapper

import ca.arnaud.hopsboilingtimer.app.alarm.AdditionNotification
import ca.arnaud.hopsboilingtimer.app.provider.TimeProviderImpl
import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import javax.inject.Inject

class AdditionNotificationMapper @Inject constructor(
    private val timeProvider: TimeProviderImpl
) : DataMapper<AdditionAlert, AdditionNotification> {

    override fun mapTo(input: AdditionAlert): AdditionNotification {
        val hops = input.additions.joinToString(separator = ", ") { it.name }
        return AdditionNotification(
            message = "Add ${input.countDown.toMinutes()}min addition: $hops",
            triggerAtMillis = timeProvider.getNowTimeMillis() + input.countDown.toMillis()
        )
    }
}