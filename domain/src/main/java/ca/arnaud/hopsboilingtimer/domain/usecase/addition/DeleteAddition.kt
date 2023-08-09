package ca.arnaud.hopsboilingtimer.domain.usecase.addition

import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import javax.inject.Inject

class DeleteAddition @Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val additionRepository: AdditionRepository,
) : SuspendableUseCase<DeleteAddition.Params, Result<Unit>>(coroutineContextProvider) {

    data class Params(
        val id: String,
    )

    override suspend fun buildRequest(params: Params): Result<Unit> {
        return additionRepository.deleteAddition(params.id)
    }

}