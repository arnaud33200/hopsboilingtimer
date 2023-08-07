package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ScheduleStateRepositoryImpl @Inject constructor() : ScheduleStateRepository {

    private val scheduleStateFlow =
        MutableStateFlow<AdditionScheduleState>(AdditionScheduleState.Idle)

    override suspend fun getScheduleStatusFlow(): Flow<AdditionScheduleState> {
        return scheduleStateFlow
    }

    override suspend fun setScheduleStatus(status: AdditionScheduleState) {
        scheduleStateFlow.value = status
    }
}