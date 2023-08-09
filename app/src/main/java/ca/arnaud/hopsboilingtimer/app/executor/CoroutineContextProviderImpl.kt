package ca.arnaud.hopsboilingtimer.app.executor

import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CoroutineContextProviderImpl @Inject constructor() : CoroutineContextProvider {

    override val context = Dispatchers.IO
}