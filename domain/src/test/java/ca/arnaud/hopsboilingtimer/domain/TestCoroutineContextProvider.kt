package ca.arnaud.hopsboilingtimer.domain

import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

class TestCoroutineContextProvider(
    override val context: CoroutineDispatcher = StandardTestDispatcher(),
) : CoroutineContextProvider {

    fun runTest(
        testBody: suspend TestScope.() -> Unit
    ) = kotlinx.coroutines.test.runTest(
        context = context,
        testBody = testBody
    )
}