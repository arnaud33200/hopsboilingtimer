package ca.arnaud.hopsboilingtimer.local.mapper

import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.local.entity.ScheduleEntity
import javax.inject.Inject

class ScheduleEntityMapper @Inject constructor(
    private val entityTimeMapper: EntityTimeMapper,
) : DataMapper<AdditionSchedule, ScheduleEntity> {

    override fun mapTo(input: AdditionSchedule): ScheduleEntity {
        return ScheduleEntity(
            id = input.id,
            startTime = entityTimeMapper.mapTo(input.startingTime),
            alertIds = input.alerts.map { it.id }
        )
    }
}