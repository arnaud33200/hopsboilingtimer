package ca.arnaud.hopsboilingtimer.domain.model

sealed interface AdditionAlert {

    val triggerAtTime: Long

    data class Start(
        override val triggerAtTime: Long,
        val additions: List<Addition>,
    ) : AdditionAlert

    data class Next(
        override val triggerAtTime: Long,
        val additions: List<Addition>,
    ) : AdditionAlert

    data class End(
        override val triggerAtTime: Long,
    ) : AdditionAlert
}

