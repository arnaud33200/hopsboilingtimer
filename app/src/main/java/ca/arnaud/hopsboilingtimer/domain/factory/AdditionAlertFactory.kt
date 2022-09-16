package ca.arnaud.hopsboilingtimer.domain.factory

import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import javax.inject.Inject

class AdditionAlertFactory @Inject constructor() {

    fun create(inputs: List<Addition>, startTime: Long): List<AdditionAlert> {
        if (inputs.isEmpty()) {
            return emptyList()
        }

        val maxDuration = inputs.maxOf { it.duration }
        return inputs.groupBy { it.duration }.map { (duration, additions) ->
            val countDown = maxDuration - duration
            AdditionAlert(
                countDown = countDown,
                triggerAtTime = startTime + countDown.toMillis(),
                additions = additions
            )
        }.sortedBy { it.countDown }
    }
}