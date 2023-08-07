package ca.arnaud.hopsboilingtimer.domain.statemachine

typealias StateId = String

interface MachineState {
    val id: StateId
}

typealias EventId = String

interface MachineEvent {
    val id: EventId
}

interface MachineParams

interface Transition<State : MachineState, Event : MachineEvent, Params : MachineParams> {
    val fromState: State
    val toState: State
    val event: Event
    val params: Params?
}

interface StateMachine<State : MachineState, Event : MachineEvent, Params : MachineParams> {

    fun transition(
        fromState: State,
        event: Event,
        params: Params? = null,
    ): Transition<State, Event, Params>?
}