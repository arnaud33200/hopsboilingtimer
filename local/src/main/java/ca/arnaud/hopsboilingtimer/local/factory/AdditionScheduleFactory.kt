package ca.arnaud.hopsboilingtimer.local.factory

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.local.entity.ScheduleEntity
import ca.arnaud.hopsboilingtimer.local.mapper.EntityTimeMapper
import java.time.LocalDateTime
import javax.inject.Inject

class AdditionScheduleFactory @Inject constructor(
    private val entityTimeMapper: EntityTimeMapper,
) {

    fun create(scheduleEntity: ScheduleEntity, alerts: List<AdditionAlert>): AdditionSchedule {
        return AdditionSchedule(
            startingTime = entityTimeMapper.mapFrom(scheduleEntity.startTime) ?: LocalDateTime.MIN,
            alerts = alerts,
            pauseTime = entityTimeMapper.mapFrom(scheduleEntity.pauseTime)
        )
    }
}