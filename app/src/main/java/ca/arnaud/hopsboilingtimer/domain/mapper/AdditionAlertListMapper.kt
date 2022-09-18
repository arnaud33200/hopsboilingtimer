package ca.arnaud.hopsboilingtimer.domain.mapper

import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import javax.inject.Inject

class AdditionAlertListMapper @Inject constructor(
    private val timeProvider: TimeProvider
) : DataMapper<List<Addition>, List<AdditionAlert>> {

    override fun mapTo(input: List<Addition>): List<AdditionAlert> {
        if (input.isEmpty()) {
            return emptyList()
        }

        val maxDuration = input.maxOf { it.duration }
        return input.groupBy { it.duration }.map { (duration, additions) ->
            val countDown = maxDuration - duration
            AdditionAlert(
                triggerAtTime = timeProvider.getNowTimeMillis() + countDown.toMillis(),
                additions = additions
            )
        }.sortedBy { it.triggerAtTime }
    }
}
