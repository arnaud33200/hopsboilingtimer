package ca.arnaud.hopsboilingtimer.domain.usecase.alert

import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.setChecked
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import javax.inject.Inject

class UpdateAdditionAlert @Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val scheduleRepository: ScheduleRepository,
) : SuspendableUseCase<UpdateAdditionAlert.Params, Response<AdditionAlert, UpdateAdditionAlert.UpdateAdditionAlertException>>(
    coroutineContextProvider
) {

    data class Params(
        val alertId: String,
        val checked: Boolean? = null,
    )

    sealed class UpdateAdditionAlertException : Throwable() {

        class AdditionAlertNotFound : UpdateAdditionAlertException()
        data class FailToStoreInDatabase(
            val dataBaseException: Throwable,
        ) : UpdateAdditionAlertException()
    }

    override suspend fun buildRequest(
        params: Params,
    ): Response<AdditionAlert, UpdateAdditionAlertException> {
        val alert = scheduleRepository.getSchedule()?.alerts?.find {
            it.id == params.alertId
        } ?: return Response.ofFailure(UpdateAdditionAlertException.AdditionAlertNotFound())

        val newAlert = params.checked?.let { checked -> alert.setChecked(checked) }
            ?: return Response.ofSuccess(alert)

        return scheduleRepository.updateAdditionAlert(newAlert)
    }
}