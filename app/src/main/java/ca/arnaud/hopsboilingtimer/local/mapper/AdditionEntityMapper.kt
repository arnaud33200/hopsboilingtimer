package ca.arnaud.hopsboilingtimer.local.mapper

import ca.arnaud.hopsboilingtimer.domain.common.TwoWayMapper
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.local.entity.AdditionEntity
import java.time.Duration
import javax.inject.Inject

class AdditionEntityMapper @Inject constructor() : TwoWayMapper<Addition, AdditionEntity> {

    override fun mapTo(input: Addition): AdditionEntity {
        return AdditionEntity(
            id = input.id,
            name = input.name,
            duration = input.duration,
        )
    }

    override fun mapFrom(output: AdditionEntity): Addition {
        return Addition(
            id = output.id,
            name = output.name,
            duration = output.duration,
        )
    }
}