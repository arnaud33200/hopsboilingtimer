package ca.arnaud.hopsboilingtimer.app.executor

import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class CoroutineScopeProvider @Inject constructor(
    private val coroutineContextProvider: CoroutineContextProvider,
) {
    val scope: CoroutineScope
        get() = CoroutineScope(
            context = coroutineContextProvider.context + SupervisorJob()
        )
}