package ca.arnaud.hopsboilingtimer.local.factory

import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.local.entity.AlertEntity
import ca.arnaud.hopsboilingtimer.local.entity.AlertTypeEntity
import javax.inject.Inject

class AdditionAlertFactory @Inject constructor() {

    fun create(alertEntity: AlertEntity, additions: List<Addition>): AdditionAlert {
        return when (alertEntity.type) {
            AlertTypeEntity.Start -> createStart(alertEntity, additions)
            AlertTypeEntity.Next -> createNext(alertEntity, additions)
            AlertTypeEntity.End -> createEnd(alertEntity)
        }
    }

    private fun createStart(
        alertEntity: AlertEntity,
        additions: List<Addition>,
    ): AdditionAlert.Start {
        return AdditionAlert.Start(
            id = alertEntity.id,
            triggerAtTime = alertEntity.triggerAt,
            additions = additions,
            checked = alertEntity.checked
        )
    }

    private fun createNext(
        alertEntity: AlertEntity,
        additions: List<Addition>,
    ): AdditionAlert.Next {
        return AdditionAlert.Next(
            id = alertEntity.id,
            triggerAtTime = alertEntity.triggerAt,
            additions = additions,
            checked = alertEntity.checked
        )
    }

    private fun createEnd(
        alertEntity: AlertEntity,
    ): AdditionAlert.End {
        return AdditionAlert.End(
            id = alertEntity.id,
            triggerAtTime = alertEntity.triggerAt,
            duration = alertEntity.duration
        )
    }
}