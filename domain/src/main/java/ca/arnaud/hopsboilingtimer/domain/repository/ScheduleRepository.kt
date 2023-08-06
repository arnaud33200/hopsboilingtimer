package ca.arnaud.hopsboilingtimer.domain.repository

import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    suspend fun getAdditionSchedule(): AdditionSchedule?
    suspend fun setAdditionSchedule(schedule: AdditionSchedule)
    suspend fun deleteSchedule(schedule: AdditionSchedule)

    suspend fun getNextAlertFLow(): Flow<AdditionAlert?>
    suspend fun setNextAlert(nextAlert: AdditionAlert?)
    suspend fun updateAdditionAlert(newAlert: AdditionAlert): Response<AdditionAlert, UpdateAdditionAlert.UpdateAdditionAlertException>
}