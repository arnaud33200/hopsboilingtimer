package ca.arnaud.hopsboilingtimer.app.service

import android.util.Log
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
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

    private val tickFlow = MutableSharedFlow<Duration>()
    private var currentJob: Job? = null

    fun getTickFlow(): Flow<Duration> {
        return tickFlow
    }

    fun start() {
        currentJob = coroutineScopeProvider.scope.launch {
            if (clockFlow != null) {
                return@launch
            }
            clockFlow = flow {
                delay(duration = initialDelay)
                progressDuration += initialDelay
                while (true) {
                    Log.d("ClockService", "tick")
                    emit(progressDuration)
                    delay(period)
                    progressDuration += period
                }
            }.also { flow ->
                flow.collect { duration ->
                    tickFlow.emit(duration)
                }
            }
        }
    }

    fun reset() {
        coroutineScopeProvider.scope.launch {
            progressDuration = Duration.ZERO
            clockFlow?.lastOrNull()
            clockFlow = null
            currentJob?.cancel()
            currentJob = null
        }
    }
}