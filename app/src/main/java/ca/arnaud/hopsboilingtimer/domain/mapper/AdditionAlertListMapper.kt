package ca.arnaud.hopsboilingtimer.domain.mapper

import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.util.*
import javax.inject.Inject

class AdditionAlertListMapper @Inject constructor(
    private val timeProvider: TimeProvider,
) : DataMapper<List<Addition>, List<AdditionAlert>> {

    override fun mapTo(input: List<Addition>): List<AdditionAlert> {
        if (input.isEmpty()) {
            return emptyList()
        }

        val maxDuration = input.maxOf { it.duration }
        val durationAdditionsMap = input.sortedByDescending { it.duration }.groupBy { it.duration }

        return durationAdditionsMap.keys.mapIndexed { index, duration ->
            val id = UUID.randomUUID().toString()
            val additions = durationAdditionsMap[duration] ?: emptyList()
            val countDown = maxDuration - duration
            val triggerAt = timeProvider.getNowTimeMillis() + countDown.toMillis()
            when (index) {
                0 -> AdditionAlert.Start(id, triggerAt, additions)
                else -> AdditionAlert.Next(id, triggerAt, additions)
            }
        }.toMutableList().apply {
            if (isNotEmpty()) {
                val triggerAt = timeProvider.getNowTimeMillis() + maxDuration.toMillis()
                val id = UUID.randomUUID().toString()
                add(AdditionAlert.End(id, triggerAt, maxDuration))
            }
        }
    }
}
