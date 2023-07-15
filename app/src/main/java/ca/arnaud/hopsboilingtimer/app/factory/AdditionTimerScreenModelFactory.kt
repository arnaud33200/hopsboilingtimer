package ca.arnaud.hopsboilingtimer.app.factory

import ca.arnaud.hopsboilingtimer.app.mapper.DurationTextMapper
import ca.arnaud.hopsboilingtimer.app.mapper.RemainingTimeTextMapper
import ca.arnaud.hopsboilingtimer.app.model.BottomBarModel
import ca.arnaud.hopsboilingtimer.app.model.ButtonStyle
import ca.arnaud.hopsboilingtimer.app.model.MainScreenModel
import ca.arnaud.hopsboilingtimer.app.model.NewAdditionModel
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class AdditionTimerScreenModelFactory @Inject constructor(
    private val rowModelFactory: RowModelFactory,
    private val timeProvider: TimeProvider,
    private val durationTextMapper: DurationTextMapper,
    private val remainingTimeTextMapper: RemainingTimeTextMapper,
) {

    fun create(
        additions: List<Addition>,
        schedule: AdditionSchedule?,
        newAdditionModel: NewAdditionModel = NewAdditionModel(),
    ): MainScreenModel {
        val additionRows = when (schedule) {
            null -> additions.map { rowModelFactory.create(it) }
            else -> schedule.alerts.map {
                val currentAlert = schedule.getNextAlert(timeProvider.getNowTimeMillis())
                rowModelFactory.create(it, currentAlert)
            }
        }

        return MainScreenModel(
            additionRows = additionRows,
            newAdditionRow = when (schedule) {
                null -> newAdditionModel
                else -> null
            },
            bottomBarModel = createBottomModel(schedule, additions)
        )
    }

    private fun createBottomModel(
        schedule: AdditionSchedule?,
        additions: List<Addition>,
    ): BottomBarModel {
        val maxDuration = additions.maxOfOrNull { it.duration }
        return when (schedule) {
            null -> BottomBarModel(
                buttonTitle = "Start Timer", // TODO - hardcoded string
                buttonTime = maxDuration?.let { durationTextMapper.mapTo(it) } ?: "",
                buttonStyle = ButtonStyle.Start,
                buttonEnable = additions.isNotEmpty(),
                subButtonTitle = "Options" // TODO hardcoded string
            )
            else -> {
                val nowTime = timeProvider.getNowTimeMillis()
                val triggerAtTime = schedule.alerts.maxOfOrNull { it.triggerAtTime }
                val remainingDuration = triggerAtTime?.let { (it - nowTime).milliseconds }
                BottomBarModel(
                    buttonTitle = "Stop Timer", // TODO - hardcoded string
                    buttonTime = remainingDuration?.let { remainingTimeTextMapper.mapTo(it) } ?: "",
                    buttonStyle = ButtonStyle.Stop,
                    buttonEnable = true,
                    subButtonTitle = null
                )
            }
        }
    }
}