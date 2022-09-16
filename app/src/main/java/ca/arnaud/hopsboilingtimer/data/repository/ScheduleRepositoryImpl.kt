package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor() : ScheduleRepository {

    private val scheduleStatusFlow = MutableStateFlow(ScheduleStatus.STOP)
    private val nextAdditionAlert = MutableStateFlow<AdditionAlert?>(null)

    private var schedule: AdditionSchedule? = null

    init {
        // TODO - check current schedule and see if it's expired
    }

    override suspend fun setScheduleStatus(status: ScheduleStatus) {
        scheduleStatusFlow.value = status
        // TODO - save locally the status
    }

    override fun getScheduleStatusFlow(): StateFlow<ScheduleStatus> {
        return scheduleStatusFlow
    }

    override suspend fun setAdditionSchedule(additionSchedule: AdditionSchedule?) {
        schedule = additionSchedule
        // TODO - save locally the status
    }

    override suspend fun getAdditionSchedule(): AdditionSchedule? {
        return schedule
    }

    override suspend fun setNextAdditionAlert(alert: AdditionAlert?) {
        nextAdditionAlert.value = alert
    }

    override fun getNextAlertFLow(): StateFlow<AdditionAlert?> {
        return nextAdditionAlert
    }
}