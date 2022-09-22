package ca.arnaud.hopsboilingtimer.app.mapper

import ca.arnaud.hopsboilingtimer.app.model.NewAdditionModel
import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.AddNewAddition
import java.time.Duration
import javax.inject.Inject

class AddNewAdditionParamsMapper @Inject constructor() :
    DataMapper<NewAdditionModel, AddNewAddition.Params> {

    override fun mapTo(input: NewAdditionModel): AddNewAddition.Params {
        val durationSecs = try {
            input.duration.toFloat()
        } catch (exception: Throwable) {
            0f
        }
        return AddNewAddition.Params(
            name = input.title,
            duration = Duration.ofSeconds((durationSecs*60).toLong())
        )
    }
}