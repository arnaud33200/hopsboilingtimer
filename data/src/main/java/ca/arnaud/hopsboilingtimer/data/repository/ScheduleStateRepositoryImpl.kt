package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ScheduleStateRepositoryImpl @Inject constructor() : ScheduleStateRepository {

    private val scheduleStatusFlow = MutableStateFlow<ScheduleStatus>(ScheduleStatus.Iddle)

    override suspend fun getScheduleStatusFlow(): Flow<ScheduleStatus> {
        return scheduleStatusFlow
    }

    override suspend fun setScheduleStatus(status: ScheduleStatus) {
        scheduleStatusFlow.value = status
    }
}