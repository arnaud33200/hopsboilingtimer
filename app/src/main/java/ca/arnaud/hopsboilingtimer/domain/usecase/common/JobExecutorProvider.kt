package ca.arnaud.hopsboilingtimer.domain.usecase.common

import kotlinx.coroutines.CoroutineDispatcher

interface JobExecutorProvider {
    val executionDispatcher: CoroutineDispatcher
    val observerDispatcher: CoroutineDispatcher
}