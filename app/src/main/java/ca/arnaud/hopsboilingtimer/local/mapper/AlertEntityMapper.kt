package ca.arnaud.hopsboilingtimer.local.mapper

import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.additionsOrEmpty
import ca.arnaud.hopsboilingtimer.domain.model.getDuration
import ca.arnaud.hopsboilingtimer.local.entity.AlertEntity
import ca.arnaud.hopsboilingtimer.local.entity.AlertTypeEntity
import java.time.Duration
import javax.inject.Inject

class AlertEntityMapper @Inject constructor() : DataMapper<AdditionAlert, AlertEntity> {

    override fun mapTo(input: AdditionAlert): AlertEntity {

        return AlertEntity(
            id = input.id,
            type = when (input) {
                is AdditionAlert.End -> AlertTypeEntity.End
                is AdditionAlert.Next -> AlertTypeEntity.Next
                is AdditionAlert.Start -> AlertTypeEntity.Start
            },
            triggerAt = input.triggerAtTime,
            additionIds = input.additionsOrEmpty().map { it.id },
            duration = input.getDuration() ?: Duration.ZERO
        )
    }
}