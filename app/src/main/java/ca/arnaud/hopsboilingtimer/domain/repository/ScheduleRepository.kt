package ca.arnaud.hopsboilingtimer.domain.repository

import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert
import kotlinx.coroutines.flow.StateFlow

interface ScheduleRepository {

    suspend fun getNextAlertFLow(): StateFlow<AdditionAlert?>
    suspend fun getScheduleFlow(): StateFlow<AdditionSchedule?>

    suspend fun setAdditionSchedule(additionSchedule: AdditionSchedule?)
    suspend fun deleteSchedule()
    suspend fun refreshAdditionSchedule()
    suspend fun getAdditionSchedule(): AdditionSchedule?

    suspend fun updateAdditionAlert(newAlert: AdditionAlert): Response<AdditionAlert, UpdateAdditionAlert.UpdateAdditionAlertException>
}