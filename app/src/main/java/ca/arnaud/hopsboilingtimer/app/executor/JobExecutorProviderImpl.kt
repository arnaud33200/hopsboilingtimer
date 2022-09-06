package ca.arnaud.hopsboilingtimer.app.executor

import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class JobExecutorProviderImpl @Inject constructor() : JobExecutorProvider {

    override val executionDispatcher = Dispatchers.IO

    override val observerDispatcher = Dispatchers.Main
}