package ca.arnaud.hopsboilingtimer.domain.statemachine

typealias StateId = String

interface MachineState {
    val id: StateId
}

typealias EventId = String

interface MachineEvent {
    val id: EventId
}

interface Transition<State : MachineState, Event : MachineEvent> {
    val fromState: State
    val toState: State
    val event: Event
}

interface StateMachine<State : MachineState, Event : MachineEvent> {

    fun transition(fromState: State, event: Event): Transition<State, Event>?
}