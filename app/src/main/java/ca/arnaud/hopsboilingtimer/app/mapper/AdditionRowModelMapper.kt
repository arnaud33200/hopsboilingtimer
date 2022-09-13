package ca.arnaud.hopsboilingtimer.app.mapper

import ca.arnaud.hopsboilingtimer.app.model.AdditionRowModel
import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import javax.inject.Inject

class AdditionRowModelMapper @Inject constructor() : DataMapper<Addition, AdditionRowModel> {

    override fun mapTo(input: Addition): AdditionRowModel {
        return AdditionRowModel(
            id = input.id,
            title = input.name,
            duration = input.duration.toMinutes().toString()
        )
    }
}