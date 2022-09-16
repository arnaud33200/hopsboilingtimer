package ca.arnaud.hopsboilingtimer.domain.model

data class AdditionSchedule(
    val startingTime: Long,
    val alerts: List<AdditionAlert>
)