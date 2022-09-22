package ca.arnaud.hopsboilingtimer.app.service

import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Singleton
class ClockService @Inject constructor(
    private val coroutineScopeProvider: CoroutineScopeProvider
) {

    private val initialDelay: Duration = Duration.ZERO
    private val period: Duration = 1.seconds

    private var progressDuration: Duration = Duration.ZERO
    private var clockFlow: Flow<Duration>? = null

    private val tickFlow = MutableStateFlow(Duration.ZERO)

    fun getTickFlow(): Flow<Duration> {
        return tickFlow
    }

    fun start() {
        coroutineScopeProvider.scope.launch {
            if (clockFlow != null) {
                return@launch
            }
            clockFlow = flow<Duration> {
                delay(duration = initialDelay)
                progressDuration += initialDelay
                while (true) {
                    emit(progressDuration)
                    delay(period)
                    progressDuration += period
                }
            }.also { flow ->
                flow.collect { duration ->
                    tickFlow.value = duration
                }
            }
        }
    }

    fun reset() {
        coroutineScopeProvider.scope.launch {
            progressDuration = Duration.ZERO
            clockFlow?.lastOrNull()
            clockFlow = null
        }
    }
}