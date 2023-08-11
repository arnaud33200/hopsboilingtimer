package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule

import ca.arnaud.hopsboilingtimer.domain.statemachine.ConditionalStateMachine
import ca.arnaud.hopsboilingtimer.domain.statemachine.ConditionalTransition
import ca.arnaud.hopsboilingtimer.domain.statemachine.Transition
import javax.inject.Inject

typealias AdditionScheduleTransition = Transition<AdditionScheduleState, AdditionScheduleEvent, AdditionScheduleParams>

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
    ): List<ConditionalTransition<AdditionScheduleState, AdditionScheduleParams>>? {
        return when (fromState) {
            AdditionScheduleState.Idle -> when (event) {
                AdditionScheduleEvent.StartClick -> listOf(
                    ConditionalTransition(AdditionScheduleState.Started)
                )

                AdditionScheduleEvent.CancelClick -> null // Forbidden
                AdditionScheduleEvent.TimerEnd -> null // Forbidden
                AdditionScheduleEvent.PauseClick -> null // Forbidden
                AdditionScheduleEvent.ResumeClick -> null // Forbidden
            }

            AdditionScheduleState.Canceled -> when (event) {
                AdditionScheduleEvent.StartClick -> listOf(
                    ConditionalTransition(AdditionScheduleState.Started)
                )

                AdditionScheduleEvent.CancelClick -> null // Forbidden
                AdditionScheduleEvent.TimerEnd -> null // Impossible
                AdditionScheduleEvent.PauseClick -> null // Impossible
                AdditionScheduleEvent.ResumeClick -> null // Forbidden
            }

            AdditionScheduleState.Started -> when (event) {
                AdditionScheduleEvent.StartClick -> null // No Action
                AdditionScheduleEvent.CancelClick -> listOf(
                    ConditionalTransition(AdditionScheduleState.Canceled)
                )

                AdditionScheduleEvent.TimerEnd -> listOf(
                    ConditionalTransition(AdditionScheduleState.Finished)
                )

                AdditionScheduleEvent.PauseClick -> listOf(
                    ConditionalTransition(AdditionScheduleState.Paused)
                )

                AdditionScheduleEvent.ResumeClick -> null // Forbidden
            }

            AdditionScheduleState.Finished -> when (event) {
                AdditionScheduleEvent.StartClick -> listOf(
                    ConditionalTransition(AdditionScheduleState.Started)
                )

                AdditionScheduleEvent.CancelClick -> null // Impossible
                AdditionScheduleEvent.TimerEnd -> null // Impossible
                AdditionScheduleEvent.PauseClick -> null // Impossible
                AdditionScheduleEvent.ResumeClick -> null // Forbidden
            }

            AdditionScheduleState.Paused -> when (event) {
                AdditionScheduleEvent.ResumeClick -> listOf(
                    ConditionalTransition(AdditionScheduleState.Started)
                )

                AdditionScheduleEvent.CancelClick -> listOf(
                    ConditionalTransition(AdditionScheduleState.Canceled)
                )

                AdditionScheduleEvent.TimerEnd -> listOf(
                    ConditionalTransition(AdditionScheduleState.Finished)
                )

                AdditionScheduleEvent.StartClick -> null // forbidden, use resume event
                AdditionScheduleEvent.PauseClick -> null // Forbidden
            }
        }
    }

}