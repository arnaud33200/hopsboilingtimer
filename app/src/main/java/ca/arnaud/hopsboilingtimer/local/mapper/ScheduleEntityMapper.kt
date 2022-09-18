package ca.arnaud.hopsboilingtimer.local.mapper

import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.local.entity.ScheduleEntity
import javax.inject.Inject

class ScheduleEntityMapper @Inject constructor() : DataMapper<AdditionSchedule, ScheduleEntity> {

    override fun mapTo(input: AdditionSchedule): ScheduleEntity {
        return ScheduleEntity(
            id = input.id,
            startTime = input.startingTime,
            alertIds = input.alerts.map { it.id }
        )
    }
}