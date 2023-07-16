package ca.arnaud.hopsboilingtimer.app.feature.alarm.mapper

import ca.arnaud.hopsboilingtimer.app.feature.alarm.model.AdditionNotificationModel
import ca.arnaud.hopsboilingtimer.app.feature.common.mapper.DurationTextMapper
import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import java.time.Duration
import javax.inject.Inject

class AdditionNotificationModelMapper @Inject constructor(
    private val durationTextMapper: DurationTextMapper
) : DataMapper<AdditionAlert, AdditionNotificationModel> {

    override fun mapTo(input: AdditionAlert): AdditionNotificationModel {
        return when (input) {
            is AdditionAlert.Start -> mapStart(input)
            is AdditionAlert.Next -> mapNext(input)
            is AdditionAlert.End -> mapEnd(input)
        }
    }

    private fun mapStart(input: AdditionAlert.Start): AdditionNotificationModel {
        val additions = input.additions
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return AdditionNotificationModel(
            alertId = input.id,
            message = "Start$hops",
            triggerAtMillis = input.triggerAtTime
        )
    }

    private fun mapNext(input: AdditionAlert.Next): AdditionNotificationModel {
        val additions = input.additions
        val duration = input.additions.firstOrNull()?.duration ?: Duration.ZERO
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return AdditionNotificationModel(
            alertId = input.id,
            message = "Next Additions (${durationTextMapper.mapTo(duration)})$hops",
            triggerAtMillis = input.triggerAtTime
        )
    }

    private fun mapEnd(input: AdditionAlert.End): AdditionNotificationModel {
        return AdditionNotificationModel(
            alertId = input.id,
            message = "Stop Boiling!",
            triggerAtMillis = input.triggerAtTime
        )
    }
}