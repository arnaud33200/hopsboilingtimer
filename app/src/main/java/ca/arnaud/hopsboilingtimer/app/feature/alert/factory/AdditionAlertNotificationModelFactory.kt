package ca.arnaud.hopsboilingtimer.app.feature.alert.factory

import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertNotificationModel
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.BaseAdditionAlertData
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.app.formatter.time.TimeHoursTextFormatter
import javax.inject.Inject

class AdditionAlertNotificationModelFactory @Inject constructor(
    private val durationTextFormatter: DurationTextFormatter,
    private val timeHoursTextFormatter: TimeHoursTextFormatter,
) {

    fun create(alert: AdditionAlertData): AdditionAlertNotificationModel {
        val comingAlert = alert.comingAlert
        return AdditionAlertNotificationModel(
            title = when (comingAlert.type) {
                AdditionAlertDataType.START -> createStartMessage(comingAlert)
                AdditionAlertDataType.NEXT -> createNextMessage(comingAlert)
                AdditionAlertDataType.END -> createEndMessage(comingAlert)
            },
            duration = "at ${timeHoursTextFormatter.format(comingAlert.scheduleAt)}"
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
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return "Next Additions (${durationTextFormatter.format(duration)})$hops"
    }

    private fun createEndMessage(input: BaseAdditionAlertData): String {
        return "Stop Boiling!"
    }
}