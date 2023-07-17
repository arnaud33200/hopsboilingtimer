package ca.arnaud.hopsboilingtimer.domain.factory

import ca.arnaud.hopsboilingtimer.domain.mapper.AdditionAlertListMapper
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import java.time.LocalDateTime
import javax.inject.Inject

class AdditionScheduleFactory @Inject constructor(
    private val additionAlertListMapper: AdditionAlertListMapper
) {

    fun create(additions: List<Addition>, startTime: LocalDateTime): AdditionSchedule {
        return AdditionSchedule(
            startingTime = startTime,
            alerts = additionAlertListMapper.mapTo(additions)
        )
    }
}