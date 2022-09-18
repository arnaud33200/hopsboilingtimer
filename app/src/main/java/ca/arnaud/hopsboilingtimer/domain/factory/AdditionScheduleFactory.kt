package ca.arnaud.hopsboilingtimer.domain.factory

import ca.arnaud.hopsboilingtimer.domain.mapper.AdditionAlertListMapper
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import javax.inject.Inject

class AdditionScheduleFactory @Inject constructor(
    private val additionAlertListMapper: AdditionAlertListMapper
) {

    fun create(additions: List<Addition>, startTime: Long): AdditionSchedule {
        return AdditionSchedule(
            startingTime = startTime,
            alerts = additionAlertListMapper.mapTo(additions) // use factory? maybe fine for now
        )
    }
}