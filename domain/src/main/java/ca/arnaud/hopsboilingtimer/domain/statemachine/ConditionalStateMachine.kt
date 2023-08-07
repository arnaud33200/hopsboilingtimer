package ca.arnaud.hopsboilingtimer.domain.statemachine

import ca.arnaud.hopsboilingtimer.domain.extension.associateByNotNull

data class ConditionalTransition<State : MachineState>(
    val toState: State,
    val condition: (() -> Boolean)? = null,
)

abstract class ConditionalStateMachine<State : MachineState, Event : MachineEvent, Params : MachineParams> :
    StateMachine<State, Event, Params> {

    private val _transitions: Map<StateId, Map<EventId, List<ConditionalTransition<State>>>> =
        getStates().associateByNotNull(keyTransform = { it.id }) { stateKey, state ->
            getEvents().associateByNotNull(keyTransform = { it.id }) { eventKey, event ->
                getTransitions(state, event)
            }.takeIf { it.isNotEmpty() }
        }

    protected abstract fun getStates(): List<State>

    protected abstract fun getEvents(): List<Event>

    protected abstract fun getTransitions(
        fromState: State,
        event: Event
    ): List<ConditionalTransition<State>>?

    override fun transition(
        fromState: State,
        event: Event,
        params: Params?,
    ): Transition<State, Event, Params>? {
        val events = _transitions[fromState.id] ?: emptyMap()
        val transitions = events[event.id] ?: emptyList()
        return transitions.firstOrNull { transition ->
            transition.condition?.invoke() ?: true
        }?.toTransition(
            event = event,
            fromState = fromState,
            params = params,
        )
    }

    private fun <State : MachineState, Event : MachineEvent, Params : MachineParams> ConditionalTransition<State>.toTransition(
        event: Event,
        fromState: State,
        params: Params? = null,
    ): Transition<State, Event, Params> {
        return object : Transition<State, Event, Params> {
            override val fromState: State = fromState
            override val toState: State = this@toTransition.toState
            override val event: Event = event
            override val params: Params? = params
        }
    }

    override fun toString(): String {
        return StringBuilder().apply {
            getStates().forEach { state ->
                getEvents().forEach { event ->
                    transition(state, event)?.let { transition ->
                        append("${state.toNameString()} + ${event.toNameString()} --> ${transition.toState.toNameString()}\n")
                    }
                }
            }
        }.toString()
    }

    private fun Any.toNameString(): String {
        return this.javaClass.toString().split(".").last().split("$").last()
    }
}