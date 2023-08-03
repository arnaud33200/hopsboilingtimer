package ca.arnaud.hopsboilingtimer.domain.usecase.common

import kotlinx.coroutines.withContext

abstract class NoParamsSuspendableUseCase<out S>(
    private val jobExecutorProvider: JobExecutorProvider,
) {

    suspend fun execute(): S {
        return withContext(jobExecutorProvider.executionDispatcher) {
            return@withContext buildRequest()
        }
    }

    protected abstract suspend fun buildRequest(): S
}