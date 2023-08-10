package ca.arnaud.hopsboilingtimer.app.feature.alert.factory

import androidx.annotation.RawRes
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.NowAlertNotificationModel
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.app.provider.StringProvider
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.additionsOrEmpty
import ca.arnaud.hopsboilingtimer.domain.model.getDuration
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.Duration
import javax.inject.Inject

class NowAlertNotificationModelFactory @Inject constructor(
    private val stringProvider: StringProvider,
    private val timeProvider: TimeProvider,
    private val durationTextFormatter: DurationTextFormatter,
) {

    enum class NowAlertType {
        Start, Reminder, Now, End
    }

    fun createAddHops(
        currentAlertData: AdditionAlertData,
        schedule: AdditionSchedule?,
    ): NowAlertNotificationModel? {
        val currentAlert = schedule?.alerts?.find { alert ->
            alert.id == currentAlertData.id
        } ?: return null

        val type = getNowAlertType(currentAlertData, currentAlert)

        return when (currentAlertData.type) {
            AdditionAlertDataType.Alert -> getAddHopsModel(currentAlert, type.toRawRes())
            AdditionAlertDataType.Reminder -> getComingSoonModel(currentAlert, type.toRawRes())
        }
    }

    fun createEnd(): NowAlertNotificationModel {
        return NowAlertNotificationModel(
            title = stringProvider.get(R.string.notification_stop_boiling),
            soundRes = NowAlertType.End.toRawRes(),
        )
    }

    private fun getNowAlertType(
        alertData: AdditionAlertData,
        alert: AdditionAlert?,
    ): NowAlertType {
        return when (alert) {
            is AdditionAlert.Start -> NowAlertType.Start
            is AdditionAlert.End -> NowAlertType.End
            is AdditionAlert.Next,
            null -> when (alertData.type) {
                AdditionAlertDataType.Reminder -> NowAlertType.Reminder
                AdditionAlertDataType.Alert -> NowAlertType.Now
            }
        }
    }

    private fun getAddHopsModel(
        alert: AdditionAlert,
        @RawRes soundRes: Int?,
    ): NowAlertNotificationModel {
        val hops = alert.getHopsList()
        val duration = alert.getDuration() ?: Duration.ZERO
        val text = stringProvider.get(
            R.string.notification_now_add_hops_text,
            hops,
            durationTextFormatter.format(duration),
        )
        return NowAlertNotificationModel(
            title = stringProvider.get(R.string.notification_now_add_hops_title),
            text = text,
            soundRes = soundRes,
        )
    }

    private fun getComingSoonModel(
        alert: AdditionAlert,
        @RawRes soundRes: Int?,
    ): NowAlertNotificationModel {
        val remainingDuration = Duration.between(
            timeProvider.getNowLocalDateTime(), alert.triggerAtTime
        )
        val title = stringProvider.get(
            R.string.notification_now_coming_soon_title,
            durationTextFormatter.format(remainingDuration)
        )

        return NowAlertNotificationModel(
            title = title,
            text = alert.getHopsList(),
            soundRes = soundRes,
        )
    }

    private fun AdditionAlert.getHopsList(): String {
        val additions = additionsOrEmpty()
        return additions.joinToString(separator = ", ", prefix = "") { it.name }
    }

    private fun NowAlertType.toRawRes(): Int? {
        return when (this) {
            NowAlertType.Start -> R.raw.schedule_start
            NowAlertType.Reminder -> R.raw.schedule_reminder
            NowAlertType.Now -> R.raw.schedule_add_now
            NowAlertType.End -> R.raw.schedule_end
        }
    }
}