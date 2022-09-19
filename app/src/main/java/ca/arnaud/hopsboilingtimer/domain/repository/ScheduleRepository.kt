package ca.arnaud.hopsboilingtimer.domain.repository

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.ScheduleStatus
import kotlinx.coroutines.flow.StateFlow

interface ScheduleRepository {

    suspend fun getNextAlertFLow(): StateFlow<AdditionAlert?>
    suspend fun getScheduleFlow(): StateFlow<AdditionSchedule?>

    suspend fun setAdditionSchedule(additionSchedule: AdditionSchedule?)
    suspend fun refreshAdditionSchedule()
    suspend fun getAdditionSchedule(): AdditionSchedule?
}