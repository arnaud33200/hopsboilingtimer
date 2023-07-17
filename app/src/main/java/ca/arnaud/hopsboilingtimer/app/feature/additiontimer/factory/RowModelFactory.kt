package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.factory

import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.app.formatter.time.RemainingTimeTextFormatter
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionOptionType
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.RowModel
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.additionsOrEmpty
import ca.arnaud.hopsboilingtimer.domain.model.isChecked
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.Duration
import javax.inject.Inject

class RowModelFactory @Inject constructor(
    private val durationTextFormatter: DurationTextFormatter,
    private val timeProvider: TimeProvider,
    private val remainingTimeTextFormatter: RemainingTimeTextFormatter,
) {

    fun create(addition: Addition): RowModel {
        return RowModel.AdditionRowModel(
            id = addition.id,
            title = addition.name,
            duration = durationTextFormatter.format(addition.duration),
            options = listOf(AdditionOptionType.Delete)
        )
    }

    fun create(
        alert: AdditionAlert,
        currentAlert: AdditionAlert?,
    ): RowModel {
        val nowTime = timeProvider.getNowLocalDateTime()
        val remainingDuration = Duration.between(nowTime, alert.triggerAtTime)
        val expired = remainingDuration.isNegative
        val countdown = remainingTimeTextFormatter.format(remainingDuration)
        val highlighted = alert == currentAlert
        if (alert is AdditionAlert.End) {

            return RowModel.AlertRowModel(
                id = alert.id,
                title = "END of boiling", // TODO - Hardcoded string
                duration = countdown,
                disabled = expired,
                highlighted = highlighted
            )
        }

        val title = alert.additionsOrEmpty().joinToString(separator = ", ") { it.name }

        val addChecked = when {
            !expired -> null
            else -> alert.isChecked()
        }

        val alertDuration = when {
            addChecked == false -> "Added?"
            expired -> "Done" // TODO - Hardcoded string
            highlighted -> "Add in $countdown" // TODO - Hardcoded string
            else -> countdown
        }

        val additionDuration = alert.additionsOrEmpty().firstOrNull()?.let { addition ->
            durationTextFormatter.format(addition.duration)
        } ?: ""

        return RowModel.AlertRowModel(
            id = alert.id,
            title = "$title ($additionDuration)",
            duration = alertDuration,
            disabled = expired,
            highlighted = highlighted,
            addChecked = addChecked
        )
    }
}