package ca.arnaud.hopsboilingtimer.data.datasource

import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert

interface ScheduleLocalDataSource {

    suspend fun getSchedule() : AdditionSchedule?

    suspend fun setSchedule(schedule: AdditionSchedule): Result<AdditionSchedule>

    suspend fun deleteSchedule(schedule: AdditionSchedule): Result<Unit>

    suspend fun updateAdditionAlert(newAlert: AdditionAlert): Response<AdditionAlert, UpdateAdditionAlert.UpdateAdditionAlertException>
}