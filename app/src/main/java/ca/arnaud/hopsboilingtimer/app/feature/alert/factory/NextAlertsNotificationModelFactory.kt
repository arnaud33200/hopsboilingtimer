package ca.arnaud.hopsboilingtimer.app.feature.alert.factory

import androidx.annotation.RawRes
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.NextAlertNotificationRowModel
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.NextAlertsNotificationModel
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.app.formatter.time.TimeHoursTextFormatter
import ca.arnaud.hopsboilingtimer.app.provider.StringProvider
import ca.arnaud.hopsboilingtimer.domain.extension.getNextAlerts
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.additionsOrEmpty
import ca.arnaud.hopsboilingtimer.domain.model.getDuration
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import java.time.Duration
import javax.inject.Inject

class NextAlertsNotificationModelFactory @Inject constructor(
    private val durationTextFormatter: DurationTextFormatter,
    private val timeHoursTextFormatter: TimeHoursTextFormatter,
    private val stringProvider: StringProvider,
) {

    fun create(
        currentAlertData: AdditionAlertData,
        schedule: AdditionSchedule?
    ): NextAlertsNotificationModel? {
        val currentAlert = schedule?.alerts?.find { alert ->
            alert.id == currentAlertData.id
        } ?: return null
        val alerts = currentAlert.getNextAlerts(schedule)
            .filter { it !is AdditionAlert.Start }
            .filterIndexed { index, _ -> index <= 1 }
        val dismissible = false

        val rows = alerts.map { baseAlertData ->
            createRow(baseAlertData, getRowType(baseAlertData, alerts))
        }.takeIf { it.isNotEmpty() }
            ?: return null

        return NextAlertsNotificationModel(
            rows = rows,
            dismissible = dismissible,
            soundRes = getSoundRes(currentAlertData, currentAlert),
        )
    }

    private fun getRowType(
        alert: AdditionAlert,
        alerts: List<AdditionAlert>,
    ): RowType {
        val duration = alert.getDuration() ?: Duration.ZERO
        return when {
            alerts.indexOf(alert) == 1 -> RowType.After
            duration.isZero -> RowType.Now
            else -> RowType.Next
        }
    }

    private enum class RowType {
        Next, After, Now
    }

    private fun RowType.toTypeText(): String {
        return stringProvider.get(
            when (this) {
                RowType.Next -> R.string.notification_next_alerts_type_next
                RowType.After -> R.string.notification_next_alerts_type_after
                RowType.Now -> R.string.notification_next_alerts_type_now
            }
        )
    }

    private fun createRow(alert: AdditionAlert, type: RowType): NextAlertNotificationRowModel {
        return NextAlertNotificationRowModel(
            id = alert.id,
            type = type.toTypeText(),
            title = when (alert) {
                is AdditionAlert.Start -> "" // won't be shown
                is AdditionAlert.Next -> createNextMessage(alert)
                is AdditionAlert.End -> createEndMessage(alert)
            },
            time = when (type) {
                RowType.Next,
                RowType.After -> {
                    stringProvider.get(
                        R.string.time_at_time,
                        timeHoursTextFormatter.format(alert.triggerAtTime)
                    )
                }

                RowType.Now -> ""
            },
        )
    }

    private fun createNextMessage(input: AdditionAlert): String {
        val additions = input.additionsOrEmpty()
        val duration = input.getDuration() ?: Duration.ZERO
        val hops = additions.joinToString(separator = ", ", prefix = "") { it.name }
        return stringProvider.get(
            R.string.notification_next_alerts_add_hops,
            hops,
            durationTextFormatter.format(duration),
        )
    }

    private fun createEndMessage(input: AdditionAlert): String {
        return stringProvider.get(R.string.notification_next_alerts_stop_boiling)
    }

    @RawRes
    private fun getSoundRes(alertData: AdditionAlertData?, alert: AdditionAlert?): Int? {
        return null // has been moved in the now alert notification
    }
}