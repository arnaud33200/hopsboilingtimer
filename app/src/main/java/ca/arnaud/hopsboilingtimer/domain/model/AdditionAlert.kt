package ca.arnaud.hopsboilingtimer.domain.model

sealed interface AdditionAlert {

    val id: String
    val triggerAtTime: Long

    data class Start(
        override val id: String,
        override val triggerAtTime: Long,
        val additions: List<Addition>,
    ) : AdditionAlert

    data class Next(
        override val id: String,
        override val triggerAtTime: Long,
        val additions: List<Addition>,
    ) : AdditionAlert

    data class End(
        override val id: String,
        override val triggerAtTime: Long,
    ) : AdditionAlert
}

fun AdditionAlert.additionsOrEmpty(): List<Addition> {
    return when (this) {
        is AdditionAlert.End -> emptyList()
        is AdditionAlert.Next -> additions
        is AdditionAlert.Start -> additions
    }
}

