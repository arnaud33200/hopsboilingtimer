package ca.arnaud.hopsboilingtimer.domain.usecase.common

import kotlinx.coroutines.withContext

abstract class SuspendableUseCase<in T, out S>(
    private val coroutineContextProvider: CoroutineContextProvider,
) {

    suspend fun execute(params: T): S {
        return withContext(coroutineContextProvider.context) {
            return@withContext buildRequest(params)
        }
    }

    protected abstract suspend fun buildRequest(params: T): S
}