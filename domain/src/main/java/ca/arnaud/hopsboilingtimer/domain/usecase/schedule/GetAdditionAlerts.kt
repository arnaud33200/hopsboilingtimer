package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.mapper.AdditionAlertListMapper
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import javax.inject.Inject

class GetAdditionAlerts @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val getAdditions: GetAdditions,
    private val additionAlertListMapper: AdditionAlertListMapper
) : SuspendableUseCase<Unit, List<AdditionAlert>>(jobExecutorProvider) {

    override suspend fun buildRequest(params: Unit): List<AdditionAlert> {
        val additions = getAdditions.execute(Unit).getOrDefault(emptyList())
        return additionAlertListMapper.mapTo(additions)
    }
}
