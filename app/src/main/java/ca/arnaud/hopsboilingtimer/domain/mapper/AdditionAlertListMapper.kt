package ca.arnaud.hopsboilingtimer.domain.mapper

import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import java.time.Duration

class AdditionAlertListMapper : DataMapper<List<Addition>, List<AdditionAlert>> {

    override fun mapTo(input: List<Addition>): List<AdditionAlert> {
        if (input.isEmpty()) {
            return emptyList()
        }

        val maxDuration = input.maxOf { it.duration }
        return input.groupBy { it.duration }.map { (duration, additions) ->
            AdditionAlert(
                countDown = maxDuration - duration,
                additions = additions
            )
        }.sortedBy { it.countDown }
    }
}
