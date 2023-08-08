package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.factory

import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionOptionType
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionRowModel
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import javax.inject.Inject

class AdditionRowModelFactory @Inject constructor(
    private val durationTextFormatter: DurationTextFormatter,
) {

    fun create(addition: Addition): AdditionRowModel {
        return AdditionRowModel(
            id = addition.id,
            title = addition.name,
            duration = durationTextFormatter.format(addition.duration),
            options = listOf(AdditionOptionType.Delete)
        )
    }
}