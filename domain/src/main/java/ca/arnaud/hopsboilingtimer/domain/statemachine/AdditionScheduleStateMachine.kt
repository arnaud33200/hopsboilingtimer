package ca.arnaud.hopsboilingtimer.domain.statemachine

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleOptions
import javax.inject.Inject

// TODO - merge with ScheduleStatus?
sealed interface AdditionScheduleState : MachineState {

    object Iddle : AdditionScheduleState
    object Stopped : AdditionScheduleState
    object Going : AdditionScheduleState
    object Canceled : AdditionScheduleState
}

sealed interface AdditionScheduleEvent : MachineEvent {

    data class TimerStart(
        val params: ScheduleOptions,
    ) : AdditionScheduleEvent
    object Cancel : AdditionScheduleEvent
    object TimerEnd : AdditionScheduleEvent
}


class AdditionScheduleStateMachine @Inject constructor() :
    ConditionalStateMachine<AdditionScheduleState, AdditionScheduleEvent>() {

    override fun getStates(): List<AdditionScheduleState> {
        return AdditionScheduleState::class.nestedClasses.map { it.objectInstance }
            .filterIsInstance<AdditionScheduleState>()
    }

    override fun getEvents(): List<AdditionScheduleEvent> {
        return AdditionScheduleEvent::class.nestedClasses.map { it.objectInstance }
            .filterIsInstance<AdditionScheduleEvent>()
    }

    override fun getTransitions(
        fromState: AdditionScheduleState,
        event: AdditionScheduleEvent
    ): List<ConditionalTransition<AdditionScheduleState, AdditionScheduleEvent>>? {
        return when (fromState) {
            AdditionScheduleState.Iddle -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    AdditionScheduleState.Going.toConditionalTransition(event, fromState)
                )
                AdditionScheduleEvent.Cancel -> null // Forbidden
                AdditionScheduleEvent.TimerEnd -> null // Forbidden
            }

            AdditionScheduleState.Canceled -> when (event) {
                is AdditionScheduleEvent.TimerStart -> listOf(
                    AdditionScheduleState.Going.toConditionalTransition(event, fromState)
                )
                AdditionScheduleEvent.Cancel -> null // No Action
                AdditionScheduleEvent.TimerEnd -> null // Impossible
            }

            AdditionScheduleState.Going -> when (event) {
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
                    AdditionScheduleState.Going.toConditionalTransition(event, fromState)
                )
                AdditionScheduleEvent.Cancel -> null // Impossible
                AdditionScheduleEvent.TimerEnd -> null // No Action
            }
        }
    }

}