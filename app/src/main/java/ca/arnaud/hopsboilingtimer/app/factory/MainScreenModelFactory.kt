package ca.arnaud.hopsboilingtimer.app.factory

import ca.arnaud.hopsboilingtimer.app.model.*
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import javax.inject.Inject

class MainScreenModelFactory @Inject constructor(
    private val additionRowModelFactory: AdditionRowModelFactory,
) {

    fun create(
        additions: List<Addition>,
        schedule: AdditionSchedule?,
        newAdditionModel: NewAdditionModel = NewAdditionModel(),
    ): MainScreenModel {
        val options = when (schedule) {
            null -> listOf(AdditionOptionType.Delete)
            else -> emptyList()
        }
        return MainScreenModel(
            additionRows = additions.map { additionRowModelFactory.create(it, options) },
            newAdditionRow = when (schedule) {
                null -> newAdditionModel
                else -> null
            },
            bottomBarModel = createBottomModel(schedule)
        )
    }

    fun createBottomModel(schedule: AdditionSchedule?): BottomBarModel {
        return when (schedule) {
            null -> BottomBarModel(
                buttonTitle = "Start Timer", // TODO - hardcoded string
                buttonStyle = ButtonStyle.Start
            )
            else -> {
                BottomBarModel(
                    buttonTitle = "Stop Timer", // TODO - hardcoded string
                    buttonStyle = ButtonStyle.Stop
                )
            }
        }
    }
}