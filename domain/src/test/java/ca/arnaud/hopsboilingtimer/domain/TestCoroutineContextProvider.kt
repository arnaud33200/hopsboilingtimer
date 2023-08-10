package ca.arnaud.hopsboilingtimer.domain

import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TestCoroutineContextProvider(
    override val context: CoroutineDispatcher = StandardTestDispatcher(),
) : CoroutineContextProvider {

    companion object {
        val DEFAULT_TIMEOUT = 10.seconds
    }

    fun runTest(
        timeout: Duration = DEFAULT_TIMEOUT,
        testBody: suspend TestScope.() -> Unit,
    ) = kotlinx.coroutines.test.runTest(
        context = context,
        timeout = timeout,
        testBody = testBody,
    )
}