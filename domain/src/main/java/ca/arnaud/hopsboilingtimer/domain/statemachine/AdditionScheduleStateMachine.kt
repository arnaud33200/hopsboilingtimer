package ca.arnaud.hopsboilingtimer.domain.statemachine

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleOptions
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleState
import javax.inject.Inject

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
    ConditionalStateMachine<ScheduleState, AdditionScheduleEvent, AdditionScheduleParams>() {

    override fun getStates(): List<ScheduleState> {
        return ScheduleState::class.nestedClasses.map { it.objectInstance }
            .filterIsInstance<ScheduleState>()
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
        fromState: ScheduleState,
        event: AdditionScheduleEvent
    ): List<ConditionalTransition<ScheduleState>>? {
        return when (fromState) {
            ScheduleState.Idle -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    ConditionalTransition(ScheduleState.Started)
                )

                AdditionScheduleEvent.Cancel -> null // Forbidden
                AdditionScheduleEvent.TimerEnd -> null // Forbidden
            }

            ScheduleState.Canceled -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    ConditionalTransition(ScheduleState.Started)
                )

                AdditionScheduleEvent.Cancel -> null // No Action
                AdditionScheduleEvent.TimerEnd -> null // Impossible
            }

            ScheduleState.Started -> when (event) {
                is AdditionScheduleEvent.TimerStart -> null // No Action
                AdditionScheduleEvent.Cancel -> listOf(
                    ConditionalTransition(ScheduleState.Canceled)
                )

                AdditionScheduleEvent.TimerEnd -> listOf(
                    ConditionalTransition(ScheduleState.Stopped)
                )
            }

            ScheduleState.Stopped -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    ConditionalTransition(ScheduleState.Started)
                )

                AdditionScheduleEvent.Cancel -> null // Impossible
                AdditionScheduleEvent.TimerEnd -> null // No Action
            }
        }
    }

}