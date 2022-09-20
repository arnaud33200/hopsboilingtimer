package ca.arnaud.hopsboilingtimer.data.datasource

import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule

interface ScheduleLocalDataSource {

    suspend fun getSchedule() : AdditionSchedule?

    suspend fun setSchedule(schedule: AdditionSchedule): Result<AdditionSchedule>

    suspend fun deleteSchedule(schedule: AdditionSchedule): Result<Unit>
}