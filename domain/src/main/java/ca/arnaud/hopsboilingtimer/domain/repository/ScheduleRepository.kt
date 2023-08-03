package ca.arnaud.hopsboilingtimer.domain.repository

import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ScheduleRepository {

    suspend fun getNextAlertFLow(): Flow<AdditionAlert?>
    suspend fun getScheduleFlow(): Flow<ScheduleStatus>

    suspend fun setAdditionScheduleStatus(status: ScheduleStatus)

    suspend fun setNextAlert(nextAlert: AdditionAlert)
    suspend fun getAdditionSchedule(): AdditionSchedule?

    suspend fun updateAdditionAlert(newAlert: AdditionAlert): Response<AdditionAlert, UpdateAdditionAlert.UpdateAdditionAlertException>
}