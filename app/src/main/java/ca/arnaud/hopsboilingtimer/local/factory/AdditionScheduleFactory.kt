package ca.arnaud.hopsboilingtimer.local.factory

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.local.entity.ScheduleEntity
import javax.inject.Inject

class AdditionScheduleFactory @Inject constructor() {

    fun create(scheduleEntity: ScheduleEntity, alerts: List<AdditionAlert>): AdditionSchedule {
        return AdditionSchedule(
            startingTime = scheduleEntity.startTime,
            alerts = alerts
        )
    }

}