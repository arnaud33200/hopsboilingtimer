package ca.arnaud.hopsboilingtimer.domain.usecase.common

import kotlinx.coroutines.withContext

abstract class SuspendableUseCase<in T, out S>(
    private val jobExecutorProvider: JobExecutorProvider,
) {

    suspend fun execute(params: T): S {
        return withContext(jobExecutorProvider.executionDispatcher) {
            return@withContext buildRequest(params)
        }
    }

    protected abstract suspend fun buildRequest(params: T): S
}