package ca.arnaud.hopsboilingtimer.domain.repository

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleState
import kotlinx.coroutines.flow.Flow

interface ScheduleStateRepository {

    suspend fun getScheduleStatusFlow(): Flow<ScheduleState>

    suspend fun setScheduleStatus(status: ScheduleState)
}