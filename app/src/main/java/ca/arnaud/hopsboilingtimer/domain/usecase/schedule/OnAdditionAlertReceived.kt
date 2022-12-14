package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import javax.inject.Inject

class OnAdditionAlertReceived @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleRepository: ScheduleRepository,
) : SuspendableUseCase<OnAdditionAlertReceived.Params, Unit>(jobExecutorProvider) {

    data class Params(
        val alertId: String,
    )

    override suspend fun buildRequest(params: Params) {
        val alert = scheduleRepository.getAdditionSchedule()?.alerts?.find {
            it.id == params.alertId
        }
        when (alert) {
            is AdditionAlert.Start,
            is AdditionAlert.Next,
            -> {
                // TODO - get the next event and pass it
                scheduleRepository.refreshAdditionSchedule()
            }
            is AdditionAlert.End -> {
                scheduleRepository.deleteSchedule()
            }
            null -> {
                // No-op
            }
        }

    }
}