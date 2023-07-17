package ca.arnaud.hopsboilingtimer.app.feature.alert.mapper

import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionData
import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.Duration
import javax.inject.Inject

class AdditionAlertDataMapper @Inject constructor(
    private val timeProvider: TimeProvider,
) : DataMapper<AdditionAlert, AdditionAlertData> {

    override fun mapTo(input: AdditionAlert): AdditionAlertData {
        val additions = when (input) {
            is AdditionAlert.Start -> input.additions
            is AdditionAlert.Next -> input.additions
            is AdditionAlert.End -> emptyList()
        }
        val initialDelay = Duration.between(
            timeProvider.getNowLocalDateTime(), input.triggerAtTime
        ).takeIf { !it.isNegative } ?: Duration.ZERO

        return AdditionAlertData(
            id = input.id,
            initialDelay = initialDelay,
            additions = additions.map { it.toAdditionData() },
            type = when (input) {
                is AdditionAlert.Start -> AdditionAlertDataType.START
                is AdditionAlert.Next -> AdditionAlertDataType.NEXT
                is AdditionAlert.End -> AdditionAlertDataType.END
            },
            duration = additions.firstOrNull()?.duration ?: Duration.ZERO
        )
    }

    private fun Addition.toAdditionData(): AdditionData {
        return AdditionData(
            name = name,
        )
    }
}