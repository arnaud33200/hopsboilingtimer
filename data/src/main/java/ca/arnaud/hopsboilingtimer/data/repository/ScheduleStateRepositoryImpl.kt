package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleState
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ScheduleStateRepositoryImpl @Inject constructor() : ScheduleStateRepository {

    private val scheduleStateFlow = MutableStateFlow<ScheduleState>(ScheduleState.Idle)

    override suspend fun getScheduleStatusFlow(): Flow<ScheduleState> {
        return scheduleStateFlow
    }

    override suspend fun setScheduleStatus(status: ScheduleState) {
        scheduleStateFlow.value = status
    }
}