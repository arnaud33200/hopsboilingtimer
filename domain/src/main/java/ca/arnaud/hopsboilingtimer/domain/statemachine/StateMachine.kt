package ca.arnaud.hopsboilingtimer.domain.statemachine

interface MachineState

interface MachineEvent

interface Transition<State: MachineState, Event: MachineEvent> {
    val fromState: State
    val toState: State
    val event: Event
}

interface StateMachine <State: MachineState, Event: MachineEvent> {

    fun transition(fromState: State, event: Event): Transition<State, Event>?
}