package ca.arnaud.hopsboilingtimer.domain.repository

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.ScheduleStatus
import kotlinx.coroutines.flow.StateFlow

interface ScheduleRepository {

    suspend fun setScheduleStatus(status: ScheduleStatus)
    fun getScheduleStatusFlow(): StateFlow<ScheduleStatus>

    suspend fun setAdditionSchedule(additionSchedule: AdditionSchedule?)
    suspend fun getAdditionSchedule(): AdditionSchedule?

    suspend fun setNextAdditionAlert(alert: AdditionAlert?)
    fun getNextAlertFLow(): StateFlow<AdditionAlert?>
}