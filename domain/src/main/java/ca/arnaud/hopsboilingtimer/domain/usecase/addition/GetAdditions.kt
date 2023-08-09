package ca.arnaud.hopsboilingtimer.domain.usecase.addition

import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import javax.inject.Inject

class GetAdditions @Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val additionRepository: AdditionRepository,
) : SuspendableUseCase<Unit, Result<List<Addition>>>(coroutineContextProvider) {

    override suspend fun buildRequest(params: Unit): Result<List<Addition>> {
        return additionRepository.getAdditions().map { additions ->
            additions.sortedByDescending { it.duration }
        }
    }
}