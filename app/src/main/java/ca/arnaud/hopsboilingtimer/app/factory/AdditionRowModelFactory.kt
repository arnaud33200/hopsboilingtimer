package ca.arnaud.hopsboilingtimer.app.factory

import ca.arnaud.hopsboilingtimer.app.mapper.DurationTextMapper
import ca.arnaud.hopsboilingtimer.app.model.AdditionOptionType
import ca.arnaud.hopsboilingtimer.app.model.AdditionRowModel
import ca.arnaud.hopsboilingtimer.app.model.BottomBarModel
import ca.arnaud.hopsboilingtimer.app.model.ButtonStyle
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import javax.inject.Inject

class AdditionRowModelFactory @Inject constructor(
    private val durationTextMapper: DurationTextMapper,
) {

    fun create(input: Addition, options: List<AdditionOptionType>): AdditionRowModel {
        return AdditionRowModel(
            id = input.id,
            title = input.name,
            duration = durationTextMapper.mapTo(input.duration),
            options = options
        )
    }
}