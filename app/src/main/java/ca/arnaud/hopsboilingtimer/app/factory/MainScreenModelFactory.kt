package ca.arnaud.hopsboilingtimer.app.factory

import ca.arnaud.hopsboilingtimer.app.model.BottomBarModel
import ca.arnaud.hopsboilingtimer.app.model.ButtonStyle
import ca.arnaud.hopsboilingtimer.app.model.MainScreenModel
import ca.arnaud.hopsboilingtimer.app.model.NewAdditionModel
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import javax.inject.Inject

class MainScreenModelFactory @Inject constructor(
    private val rowModelFactory: RowModelFactory,
    private val timeProvider: TimeProvider,
) {

    fun create(
        additions: List<Addition>,
        schedule: AdditionSchedule?,
        newAdditionModel: NewAdditionModel = NewAdditionModel(),
    ): MainScreenModel {
        val additionRows = when (schedule) {
            null -> additions.map { rowModelFactory.create(it) }
            else -> schedule.alerts.map {
                rowModelFactory.create(it, schedule.getNextAlert(timeProvider.getNowTimeMillis()))
            }
        }

        return MainScreenModel(
            additionRows = additionRows,
            newAdditionRow = when (schedule) {
                null -> newAdditionModel
                else -> null
            },
            bottomBarModel = createBottomModel(schedule)
        )
    }

    private fun createBottomModel(schedule: AdditionSchedule?): BottomBarModel {
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