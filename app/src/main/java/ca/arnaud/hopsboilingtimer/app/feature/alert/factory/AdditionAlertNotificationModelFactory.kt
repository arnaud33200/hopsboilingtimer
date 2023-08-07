package ca.arnaud.hopsboilingtimer.app.feature.alert.factory

import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertNotificationModel
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AlertNotificationRowModel
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

class AdditionAlertNotificationModelFactory @Inject constructor(
    private val durationTextFormatter: DurationTextFormatter,
    private val timeHoursTextFormatter: TimeHoursTextFormatter,
    private val stringProvider: StringProvider,
) {

    fun create(
        currentAlertData: AdditionAlertData,
        schedule: AdditionSchedule
    ): AdditionAlertNotificationModel {
        val currentAlert = schedule.alerts.find { it.id == currentAlertData.id }
            ?: return AdditionAlertNotificationModel()

        val alerts = currentAlert.getNextAlerts(schedule)
            .filter { it !is AdditionAlert.Start }
            .filterIndexed { index, _ -> index <= 1 }
        val dismissible = false
        return AdditionAlertNotificationModel(
            rows = alerts.map { baseAlertData ->
                createRow(baseAlertData, getRowType(baseAlertData, alerts))
            },
            dismissible = dismissible,
        )
    }

    fun createEnd(): AdditionAlertNotificationModel {
        return AdditionAlertNotificationModel(
            rows = listOf(
                AlertNotificationRowModel(
                    type = RowType.Now.toTypeText(),
                    title = stringProvider.get(R.string.notification_stop_boiling)
                )
            ),
            dismissible = true,
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
                RowType.Next -> R.string.notification_row_type_next
                RowType.After -> R.string.notification_row_type_after
                RowType.Now -> R.string.notification_row_type_now
            }
        )
    }

    private fun createRow(alert: AdditionAlert, type: RowType): AlertNotificationRowModel {
        return AlertNotificationRowModel(
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
            R.string.notification_addition_alert_message,
            hops,
            durationTextFormatter.format(duration),
        )
    }

    private fun createEndMessage(input: AdditionAlert): String {
        return stringProvider.get(R.string.notification_end_schedule)
    }
}