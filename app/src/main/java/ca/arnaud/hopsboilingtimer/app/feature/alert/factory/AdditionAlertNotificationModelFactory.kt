package ca.arnaud.hopsboilingtimer.app.feature.alert.factory

import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertNotificationModel
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AlertNotificationRowModel
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.BaseAdditionAlertData
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.app.formatter.time.TimeHoursTextFormatter
import javax.inject.Inject

class AdditionAlertNotificationModelFactory @Inject constructor(
    private val durationTextFormatter: DurationTextFormatter,
    private val timeHoursTextFormatter: TimeHoursTextFormatter,
) {

    fun create(alert: AdditionAlertData): AdditionAlertNotificationModel {
        val alerts = (listOf(alert.comingAlert) + alert.nextAlerts)
            .filter { it.type != AdditionAlertDataType.START }
            .filterIndexed { index, _ -> index <= 1 }
        return AdditionAlertNotificationModel(
            rows = alerts.map { baseAlertData ->
                createRow(baseAlertData, getRowType(baseAlertData, alerts))
            }
        )
    }

    private fun getRowType(
        alert: BaseAdditionAlertData,
        alerts: List<BaseAdditionAlertData>,
    ): RowType {
        return when {
            alerts.indexOf(alert) == 1 -> RowType.After
            alert.duration.isZero -> RowType.Now
            else -> RowType.Next
        }
    }

    private enum class RowType {
        Next, After, Now
    }

    private fun createRow(alert: BaseAdditionAlertData, type: RowType): AlertNotificationRowModel {
        return AlertNotificationRowModel(
            type = when (type) {
                // TODO - hardcoded string
                RowType.Next -> "Next"
                RowType.After -> "After"
                RowType.Now -> "Now"
            },
            title = when (alert.type) {
                AdditionAlertDataType.START -> createStartMessage(alert)
                AdditionAlertDataType.NEXT -> createNextMessage(alert)
                AdditionAlertDataType.END -> createEndMessage(alert)
            },
            time = when (type) {
                RowType.Next,
                RowType.After -> {
                    // TODO - show a countdown when 5 min left
                    "at ${timeHoursTextFormatter.format(alert.scheduleAt)}"
                }
                RowType.Now -> ""
            }
        )
    }

    private fun createStartMessage(input: BaseAdditionAlertData): String {
        val additions = input.additions
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return "Start$hops"
    }

    private fun createNextMessage(input: BaseAdditionAlertData): String {
        val additions = input.additions
        val duration = input.duration
        val hops = additions.joinToString(separator = ", ", prefix = "") { it.name }
        return "add $hops (${durationTextFormatter.format(duration)})"
    }

    private fun createEndMessage(input: BaseAdditionAlertData): String {
        return "stop boiling!"
    }
}