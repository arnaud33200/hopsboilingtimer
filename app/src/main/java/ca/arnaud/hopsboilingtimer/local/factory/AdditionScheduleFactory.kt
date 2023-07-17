package ca.arnaud.hopsboilingtimer.local.factory

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.local.entity.ScheduleEntity
import ca.arnaud.hopsboilingtimer.local.mapper.EntityTimeMapper
import javax.inject.Inject

class AdditionScheduleFactory @Inject constructor(
    private val entityTimeMapper: EntityTimeMapper,
) {

    fun create(scheduleEntity: ScheduleEntity, alerts: List<AdditionAlert>): AdditionSchedule {
        return AdditionSchedule(
            startingTime = entityTimeMapper.mapFrom(scheduleEntity.startTime),
            alerts = alerts
        )
    }
}