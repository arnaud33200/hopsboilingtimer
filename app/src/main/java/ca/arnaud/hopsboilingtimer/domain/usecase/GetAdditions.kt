package ca.arnaud.hopsboilingtimer.domain.usecase

import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import javax.inject.Inject

class GetAdditions @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val additionRepository: AdditionRepository,
) : SuspendableUseCase<Unit, Result<List<Addition>>>(jobExecutorProvider) {

    override suspend fun buildRequest(params: Unit): Result<List<Addition>> {
        return additionRepository.getAdditions()
    }
}