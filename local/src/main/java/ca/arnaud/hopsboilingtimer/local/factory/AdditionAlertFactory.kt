package ca.arnaud.hopsboilingtimer.local.factory

import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.local.entity.AlertEntity
import ca.arnaud.hopsboilingtimer.local.entity.AlertTypeEntity
import ca.arnaud.hopsboilingtimer.local.mapper.EntityTimeMapper
import java.time.LocalDateTime
import javax.inject.Inject

class AdditionAlertFactory @Inject constructor(
    private val entityTimeMapper: EntityTimeMapper,
) {

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
            triggerAtTime = alertEntity.triggerAt.toLocalDateTime(),
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
            triggerAtTime = alertEntity.triggerAt.toLocalDateTime(),
            additions = additions,
            checked = alertEntity.checked
        )
    }

    private fun createEnd(
        alertEntity: AlertEntity,
    ): AdditionAlert.End {
        return AdditionAlert.End(
            id = alertEntity.id,
            triggerAtTime = alertEntity.triggerAt.toLocalDateTime(),
            duration = alertEntity.duration
        )
    }

    private fun Long.toLocalDateTime(): LocalDateTime {
        return entityTimeMapper.mapFrom(this) ?: LocalDateTime.MIN
    }
}