package ca.arnaud.hopsboilingtimer.app.feature.alert.mapper

import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.Duration
import javax.inject.Inject

class AdditionAlertDataFactory @Inject constructor(
    private val timeProvider: TimeProvider,
) {

    fun create(input: AdditionAlert): AdditionAlertData {
        val initialDelay = Duration.between(
            timeProvider.getNowLocalDateTime(), input.triggerAtTime
        ).takeIf { !it.isNegative } ?: Duration.ZERO

        return AdditionAlertData(
            id = input.id,
            initialDelay = initialDelay,
            type = AdditionAlertDataType.Alert,
        )
    }

    fun createReminder(additionAlertData: AdditionAlertData): AdditionAlertData {
        return additionAlertData.copy(
            initialDelay = additionAlertData.initialDelay - Duration.ofSeconds(30),
            type = AdditionAlertDataType.Reminder,
        )
    }
}