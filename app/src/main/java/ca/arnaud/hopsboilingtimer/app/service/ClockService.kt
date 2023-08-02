package ca.arnaud.hopsboilingtimer.app.service

import android.util.Log
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@Singleton
class ClockService @Inject constructor(
    private val coroutineScopeProvider: CoroutineScopeProvider
) {

    private val period: Duration = 1.seconds
    private val initialDelay: Duration = Duration.ZERO
    private val totalDuration: Duration = 1.days

    private var progressDuration: Duration = Duration.ZERO
    private val tickFlow = MutableSharedFlow<Duration>()
    private var currentClockJob: Job? = null

    fun getTickFlow(): Flow<Duration> {
        return tickFlow
    }

    fun start() {
        currentClockJob = coroutineScopeProvider.scope.launch {
            delay(duration = initialDelay)
            progressDuration += initialDelay
            while (progressDuration < totalDuration) {
                Log.d("ClockService", "tick")
                tickFlow.emit(progressDuration)
                delay(period)
                progressDuration += period
            }
        }
    }

    fun reset() {
        progressDuration = Duration.ZERO
        currentClockJob?.cancel()
        currentClockJob = null
    }
}