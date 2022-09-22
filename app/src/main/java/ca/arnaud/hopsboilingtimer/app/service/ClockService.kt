package ca.arnaud.hopsboilingtimer.app.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Singleton
class ClockService @Inject constructor() {

    private val initialDelay: Duration = Duration.ZERO
    private val period: Duration = 1.seconds

    private var progressDuration: Duration = Duration.ZERO
    private var clockFlow: Flow<Duration>? = null

    private val tickFlow = MutableStateFlow(Duration.ZERO)

    fun getTickFlow(): Flow<Duration> {
        return tickFlow
    }

    suspend fun start() {
        if (clockFlow != null) {
            return
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

    suspend fun reset() {
        progressDuration = Duration.ZERO
        clockFlow?.lastOrNull()
        clockFlow = null
    }
}