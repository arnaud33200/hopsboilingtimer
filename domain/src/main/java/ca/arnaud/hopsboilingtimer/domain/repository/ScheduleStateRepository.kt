package ca.arnaud.hopsboilingtimer.domain.repository

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import kotlinx.coroutines.flow.Flow

interface ScheduleStateRepository {

    suspend fun getScheduleStatusFlow(): Flow<ScheduleStatus>

    suspend fun setScheduleStatus(status: ScheduleStatus)
}