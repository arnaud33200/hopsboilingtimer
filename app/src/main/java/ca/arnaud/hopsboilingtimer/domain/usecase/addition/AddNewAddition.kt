package ca.arnaud.hopsboilingtimer.domain.usecase.addition

import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import java.time.Duration
import javax.inject.Inject

class AddNewAddition @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val additionRepository: AdditionRepository,
) : SuspendableUseCase<AddNewAddition.Params, Result<Unit>>(jobExecutorProvider) {

    data class Params(
        val name: String,
        val duration: Duration
    )

    override suspend fun buildRequest(params: Params): Result<Unit> {
        val newAddition = Addition(
            name = params.name,
            duration = params.duration
        )
        return additionRepository.addAddition(newAddition)
    }

}