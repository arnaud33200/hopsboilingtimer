package ca.arnaud.hopsboilingtimer.domain.statemachine

import ca.arnaud.hopsboilingtimer.domain.extension.associateByNotNull

data class ConditionalTransition<State : MachineState, Event : MachineEvent>(
    val transition: Transition<State, Event>,
    val condition: (() -> Boolean)?,
)

fun <State : MachineState, Event : MachineEvent> State.toConditionalTransition(
    event: Event,
    fromState: State,
    condition: (() -> Boolean)? = null,
): ConditionalTransition<State, Event> {
    return ConditionalTransition(
        transition = object : Transition<State, Event> {
            override val fromState: State = fromState
            override val toState: State = this@toConditionalTransition
            override val event: Event = event
        },
        condition = condition,
    )
}

abstract class ConditionalStateMachine<State : MachineState, Event : MachineEvent> :
    StateMachine<State, Event> {

    private val transitions: Map<State, Map<Event, List<ConditionalTransition<State, Event>>>> =
        getStates().associateWith { state ->
            getEvents().associateByNotNull { event ->
                getTransitions(state, event)
            }
        }

    protected abstract fun getStates(): List<State>

    protected abstract fun getEvents(): List<Event>

    protected abstract fun getTransitions(
        fromState: State,
        event: Event
    ): List<ConditionalTransition<State, Event>>?

    override fun transition(fromState: State, event: Event): Transition<State, Event>? {
        return transitions[fromState]?.get(event)?.firstOrNull { transition ->
            transition.condition?.invoke() ?: true
        }?.transition
    }
}