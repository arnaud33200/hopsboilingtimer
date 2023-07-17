package ca.arnaud.hopsboilingtimer.domain.model

import java.time.Duration
import java.time.LocalDateTime

sealed interface AdditionAlert {

    val id: String
    val triggerAtTime: LocalDateTime

    data class Start(
        override val id: String,
        override val triggerAtTime: LocalDateTime,
        val additions: List<Addition>,
        val checked: Boolean
    ) : AdditionAlert

    data class Next(
        override val id: String,
        override val triggerAtTime: LocalDateTime,
        val additions: List<Addition>,
        val checked: Boolean
    ) : AdditionAlert

    data class End(
        override val id: String,
        override val triggerAtTime: LocalDateTime,
        val duration: Duration
    ) : AdditionAlert
}

fun AdditionAlert.additionsOrEmpty(): List<Addition> {
    return when (this) {
        is AdditionAlert.End -> emptyList()
        is AdditionAlert.Next -> additions
        is AdditionAlert.Start -> additions
    }
}

fun AdditionAlert.getDuration(): Duration? {
    return when (this) {
        is AdditionAlert.End -> duration
        is AdditionAlert.Start,
        is AdditionAlert.Next -> additionsOrEmpty().firstOrNull()?.duration
    }
}

fun AdditionAlert.isChecked(): Boolean? {
    return when (this) {
        is AdditionAlert.Start -> checked
        is AdditionAlert.Next -> checked
        is AdditionAlert.End -> null
    }
}

fun AdditionAlert.setChecked(checked: Boolean): AdditionAlert {
    return when (this) {
        is AdditionAlert.Start -> this.copy(checked = checked)
        is AdditionAlert.Next -> this.copy(checked = checked)
        is AdditionAlert.End -> this
    }
}
