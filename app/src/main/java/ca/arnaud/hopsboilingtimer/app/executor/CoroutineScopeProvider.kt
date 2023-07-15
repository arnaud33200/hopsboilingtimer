package ca.arnaud.hopsboilingtimer.app.executor

import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class CoroutineScopeProvider @Inject constructor(
    private val jobExecutorProvider: JobExecutorProvider,
) {
    val scope: CoroutineScope
        get() = CoroutineScope(
            context = jobExecutorProvider.executionDispatcher + SupervisorJob()
        )
}