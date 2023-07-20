package ca.arnaud.hopsboilingtimer.domain.extension

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.isValid

fun AdditionAlert.getNextAlerts(schedule: AdditionSchedule?): List<AdditionAlert> {
    val alerts = schedule?.alerts?.filter { it.isValid() } ?: emptyList()
    val alertIndex = this.takeIf { it.isValid() }?.let { validAlert ->
        alerts.indexOfFirstOrNull { alert -> validAlert.id == alert.id }
    } ?: alerts.lastIndex
    return alerts.filterIndexed { index, _ -> index > alertIndex }
}