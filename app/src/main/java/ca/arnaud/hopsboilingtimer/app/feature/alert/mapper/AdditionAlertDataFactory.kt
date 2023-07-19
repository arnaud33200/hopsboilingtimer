package ca.arnaud.hopsboilingtimer.app.feature.alert.mapper

import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.BaseAdditionAlertData
import ca.arnaud.hopsboilingtimer.domain.extension.indexOfFirstOrNull
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.isValid
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.Duration
import javax.inject.Inject

class AdditionAlertDataFactory @Inject constructor(
    private val timeProvider: TimeProvider,
) {

    fun create(input: AdditionAlert, schedule: AdditionSchedule?): AdditionAlertData {
        val alerts = schedule?.alerts?.filter { it.isValid() } ?: emptyList()
        val alertIndex = input.takeIf { it.isValid() }?.let { validAlert ->
            alerts.indexOfFirstOrNull { alert -> validAlert.id == alert.id }
        } ?: alerts.lastIndex
        val nextAlerts = alerts.filterIndexed { index, _ -> index > alertIndex }
            .map { createAdditionAlertData(it) }

        val initialDelay = Duration.between(
            timeProvider.getNowLocalDateTime(), input.triggerAtTime
        ).takeIf { !it.isNegative } ?: Duration.ZERO

        return AdditionAlertData(
            initialDelay = initialDelay,
            comingAlert = createAdditionAlertData(input),
            nextAlerts = nextAlerts,
        )
    }

    private fun createAdditionAlertData(input: AdditionAlert): BaseAdditionAlertData {
        val additions = when (input) {
            is AdditionAlert.Start -> input.additions
            is AdditionAlert.Next -> input.additions
            is AdditionAlert.End -> emptyList()
        }

        return BaseAdditionAlertData(
            id = input.id,
            additions = additions.map { it.toAdditionData() },
            type = when (input) {
                is AdditionAlert.Start -> AdditionAlertDataType.START
                is AdditionAlert.Next -> AdditionAlertDataType.NEXT
                is AdditionAlert.End -> AdditionAlertDataType.END
            },
            duration = additions.firstOrNull()?.duration ?: Duration.ZERO,
            scheduleAt = input.triggerAtTime,
        )
    }

    private fun Addition.toAdditionData(): AdditionData {
        return AdditionData(
            name = name,
        )
    }
}