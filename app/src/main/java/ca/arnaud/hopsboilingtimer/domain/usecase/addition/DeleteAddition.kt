package ca.arnaud.hopsboilingtimer.domain.usecase.addition

import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import javax.inject.Inject

class DeleteAddition @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val additionRepository: AdditionRepository,
) : SuspendableUseCase<DeleteAddition.Params, Result<Unit>>(jobExecutorProvider) {

    data class Params(
        val id: String,
    )

    override suspend fun buildRequest(params: Params): Result<Unit> {
        return additionRepository.deleteAddition(params.id)
    }

}