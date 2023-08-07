package ca.arnaud.hopsboilingtimer.domain.statemachine

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleOptions
import javax.inject.Inject

// TODO - merge with ScheduleStatus?
sealed interface AdditionScheduleState : MachineState {

    object Idle : AdditionScheduleState {
        override val id = "Idle"
    }

    object Stopped : AdditionScheduleState {
        override val id = "Stopped"
    }

    object Started : AdditionScheduleState {
        override val id = "Going"
    }

    object Canceled : AdditionScheduleState {
        override val id = "Canceled"
    }
}

sealed interface AdditionScheduleEvent : MachineEvent {

    object TimerStart : AdditionScheduleEvent {
        override val id = "TimerStart"
    }

    object Cancel : AdditionScheduleEvent {
        override val id = "Cancel"
    }

    object TimerEnd : AdditionScheduleEvent {
        override val id = "TimerEnd"
    }
}

sealed interface AdditionScheduleParams : MachineParams {

    data class Start(val scheduleOptions: ScheduleOptions) : AdditionScheduleParams
}


class AdditionScheduleStateMachine @Inject constructor() :
    ConditionalStateMachine<AdditionScheduleState, AdditionScheduleEvent, AdditionScheduleParams>() {

    override fun getStates(): List<AdditionScheduleState> {
        return AdditionScheduleState::class.nestedClasses.map { it.objectInstance }
            .filterIsInstance<AdditionScheduleState>()
    }

    override fun getEvents(): List<AdditionScheduleEvent> {
        return AdditionScheduleEvent::class.nestedClasses.map { it.objectInstance }
            .filterIsInstance<AdditionScheduleEvent>()
    }

    /**
     * Idle + TimerStart --> Going
     * Canceled + TimerStart --> Going
     * Stopped + TimerStart --> Going
     * Going + Cancel --> Canceled
     * Going + TimerEnd --> Stopped
     */
    override fun getTransitions(
        fromState: AdditionScheduleState,
        event: AdditionScheduleEvent
    ): List<ConditionalTransition<AdditionScheduleState, AdditionScheduleEvent, AdditionScheduleParams>>? {
        return when (fromState) {
            AdditionScheduleState.Idle -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    AdditionScheduleState.Started.toConditionalTransition(event, fromState)
                )

                AdditionScheduleEvent.Cancel -> null // Forbidden
                AdditionScheduleEvent.TimerEnd -> null // Forbidden
            }

            AdditionScheduleState.Canceled -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    AdditionScheduleState.Started.toConditionalTransition(event, fromState)
                )

                AdditionScheduleEvent.Cancel -> null // No Action
                AdditionScheduleEvent.TimerEnd -> null // Impossible
            }

            AdditionScheduleState.Started -> when (event) {
                is AdditionScheduleEvent.TimerStart -> null // No Action
                AdditionScheduleEvent.Cancel -> listOf(
                    AdditionScheduleState.Canceled.toConditionalTransition(event, fromState)
                )

                AdditionScheduleEvent.TimerEnd -> listOf(
                    AdditionScheduleState.Stopped.toConditionalTransition(event, fromState)
                )
            }

            AdditionScheduleState.Stopped -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    AdditionScheduleState.Started.toConditionalTransition(event, fromState)
                )

                AdditionScheduleEvent.Cancel -> null // Impossible
                AdditionScheduleEvent.TimerEnd -> null // No Action
            }
        }
    }

}