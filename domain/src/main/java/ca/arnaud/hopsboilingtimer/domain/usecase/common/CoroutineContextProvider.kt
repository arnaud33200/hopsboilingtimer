package ca.arnaud.hopsboilingtimer.domain.usecase.common

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineContextProvider {

    val context: CoroutineDispatcher
}