package ca.arnaud.hopsboilingtimer.domain.repository

import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleState
import kotlinx.coroutines.flow.Flow

interface ScheduleStateRepository {

    suspend fun getScheduleStatusFlow(): Flow<AdditionScheduleState>

    suspend fun setScheduleStatus(status: AdditionScheduleState)
}