package ca.arnaud.hopsboilingtimer.domain.usecase.common

import kotlinx.coroutines.withContext

abstract class NoParamsSuspendableUseCase<out S>(
    private val coroutineContextProvider: CoroutineContextProvider,
) {

    suspend fun execute(): S {
        return withContext(coroutineContextProvider.context) {
            return@withContext buildRequest()
        }
    }

    protected abstract suspend fun buildRequest(): S
}