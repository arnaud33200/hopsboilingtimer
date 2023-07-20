package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.factory

import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AlertRowModel
import ca.arnaud.hopsboilingtimer.app.formatter.time.RemainingTimeTextFormatter
import ca.arnaud.hopsboilingtimer.app.formatter.time.TimeHoursTextFormatter
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.additionsOrEmpty
import ca.arnaud.hopsboilingtimer.domain.model.isChecked
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.Duration
import javax.inject.Inject

class AlertRowModelFactory @Inject constructor(
    private val timeProvider: TimeProvider,
    private val remainingTimeTextFormatter: RemainingTimeTextFormatter,
    private val timeHoursTextFormatter: TimeHoursTextFormatter,
) {
    fun create(
        alert: AdditionAlert,
        currentAlert: AdditionAlert?,
    ): AlertRowModel {
        val nowTime = timeProvider.getNowLocalDateTime()
        val remainingDuration = Duration.between(nowTime, alert.triggerAtTime)
        val highlighted = alert == currentAlert

        return when {
            remainingDuration.isNegative -> createAdded(alert)
            else -> createNext(alert, highlighted, remainingDuration)
        }
    }

    private fun createNext(
        alert: AdditionAlert,
        highlighted: Boolean,
        remainingDuration: Duration
    ): AlertRowModel.Next {
        return AlertRowModel.Next(
            id = alert.id,
            title = getTitle(alert),
            time = when {
                highlighted -> "In ${remainingTimeTextFormatter.format(remainingDuration)}"
                else -> "At ${timeHoursTextFormatter.format(alert.triggerAtTime)}"
            },
            highlighted = highlighted,
        )
    }

    private fun createAdded(alert: AdditionAlert): AlertRowModel.Added {
        return AlertRowModel.Added(
            id = alert.id,
            title = getTitle(alert),
            time = timeHoursTextFormatter.format(alert.triggerAtTime),
            checked = alert.isChecked() ?: false,
        )
    }

    private fun getTitle(alert: AdditionAlert): String {
        return when (alert) {
            is AdditionAlert.End -> "End boiling" // TODO - hardcoded string
            is AdditionAlert.Next,
            is AdditionAlert.Start -> {
                alert.additionsOrEmpty().joinToString(separator = ", ") { it.name }
                    .takeIf { it.isNotBlank() }?.let { additions ->
                        "+ $additions"
                    } ?: ""
            }
        }
    }

}