package ca.arnaud.hopsboilingtimer.app.feature.alert.factory

import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertNotificationModel
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import javax.inject.Inject

class AdditionAlertNotificationModelFactory @Inject constructor(
    private val durationTextFormatter: DurationTextFormatter,
) {

    fun create(alert: AdditionAlertData): AdditionAlertNotificationModel {
        return AdditionAlertNotificationModel(
            title = when (alert.type) {
                AdditionAlertDataType.START -> createStartMessage(alert)
                AdditionAlertDataType.NEXT -> createNextMessage(alert)
                AdditionAlertDataType.END -> createEndMessage(alert)
            },
            duration = durationTextFormatter.format(alert.duration)
        )
    }

    private fun createStartMessage(input: AdditionAlertData): String {
        val additions = input.additions
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return "Start$hops"
    }

    private fun createNextMessage(input: AdditionAlertData): String {
        val additions = input.additions
        val duration = input.duration
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return "Next Additions (${durationTextFormatter.format(duration)})$hops"
    }

    private fun createEndMessage(input: AdditionAlertData): String {
        return "Stop Boiling!"
    }
}