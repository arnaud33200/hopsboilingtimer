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
    ): List<ConditionalTransition<AdditionScheduleState>>? {
        return when (fromState) {
            AdditionScheduleState.Idle -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    ConditionalTransition(AdditionScheduleState.Started)
                )

                AdditionScheduleEvent.Cancel -> null // Forbidden
                AdditionScheduleEvent.TimerEnd -> null // Forbidden
            }

            AdditionScheduleState.Canceled -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    ConditionalTransition(AdditionScheduleState.Started)
                )

                AdditionScheduleEvent.Cancel -> null // No Action
                AdditionScheduleEvent.TimerEnd -> null // Impossible
            }

            AdditionScheduleState.Started -> when (event) {
                is AdditionScheduleEvent.TimerStart -> null // No Action
                AdditionScheduleEvent.Cancel -> listOf(
                    ConditionalTransition(AdditionScheduleState.Canceled)
                )

                AdditionScheduleEvent.TimerEnd -> listOf(
                    ConditionalTransition(AdditionScheduleState.Stopped)
                )
            }

            AdditionScheduleState.Stopped -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    ConditionalTransition(AdditionScheduleState.Started)
                )

                AdditionScheduleEvent.Cancel -> null // Impossible
                AdditionScheduleEvent.TimerEnd -> null // No Action
            }
        }
    }

}