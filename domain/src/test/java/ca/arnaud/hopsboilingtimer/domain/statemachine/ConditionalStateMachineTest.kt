package ca.arnaud.hopsboilingtimer.domain.statemachine

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConditionalStateMachineTest {

    sealed interface TestState : MachineState {

        object A : TestState {
            override val id = "A"
        }

        object B : TestState {
            override val id = "B"
        }

        object C : TestState {
            override val id = "C"
        }

        object D : TestState {
            override val id = "D"
        }
    }

    sealed interface TestEvent : MachineEvent {

        object Event1 : TestEvent {
            override val id = "Event1"
        }

        object Event2 : TestEvent {
            override val id = "Event2"
        }

        object Event3 : TestEvent {
            override val id = "Event3"
        }

        object Event4 : TestEvent {
            override val id = "Event4"
        }
    }

    sealed interface TestParams : MachineParams {

        object Params1 : TestParams
    }

    private lateinit var subject: ConditionalStateMachine<TestState, TestEvent, TestParams>

    @Before
    fun setup() {
        subject = object : ConditionalStateMachine<TestState, TestEvent, TestParams>() {

            override fun getStates(): List<TestState> {
                return listOf(TestState.A, TestState.B, TestState.C, TestState.D)
            }

            override fun getEvents(): List<TestEvent> {
                return listOf(
                    TestEvent.Event1,
                    TestEvent.Event2,
                    TestEvent.Event3,
                    TestEvent.Event4
                )
            }

            /**
             * A + 1 --> B
             * A + 4 --> C
             * B + 2 --> A
             * C + 3 --> D
             */
            override fun getTransitions(
                fromState: TestState,
                event: TestEvent
            ): List<ConditionalTransition<TestState, TestParams>>? {
                return when (fromState) {
                    TestState.A -> when (event) {
                        TestEvent.Event1 -> listOf(
                            ConditionalTransition(TestState.B)
                        )

                        TestEvent.Event2 -> null
                        TestEvent.Event3 -> null
                        TestEvent.Event4 -> listOf(
                            ConditionalTransition(TestState.C)
                        )
                    }

                    TestState.B -> when (event) {
                        TestEvent.Event1 -> null
                        TestEvent.Event2 -> listOf(
                            ConditionalTransition(TestState.A)
                        )

                        TestEvent.Event3 -> null
                        TestEvent.Event4 -> null
                    }

                    TestState.C -> when (event) {
                        TestEvent.Event1 -> null
                        TestEvent.Event2 -> null
                        TestEvent.Event3 -> listOf(
                            ConditionalTransition(TestState.D)
                        )

                        TestEvent.Event4 -> null
                    }

                    TestState.D -> when (event) {
                        TestEvent.Event1 -> null
                        TestEvent.Event2 -> null
                        TestEvent.Event3 -> null
                        TestEvent.Event4 -> null
                    }
                }
            }
        }
    }

    @Test
    fun `GIVEN all the different transitions WHEN transitioning VERIFY correct state`() {
        assertEquals(TestState.B, subject.transition(TestState.A, TestEvent.Event1)!!.toState)
        assertEquals(TestState.C, subject.transition(TestState.A, TestEvent.Event4)!!.toState)
        assertEquals(TestState.A, subject.transition(TestState.B, TestEvent.Event2)!!.toState)
        assertEquals(TestState.D, subject.transition(TestState.C, TestEvent.Event3)!!.toState)
        assertEquals(TestState.B, subject.transition(TestState.A, TestEvent.Event1)!!.toState)
    }

    @Test
    fun `GIVEN params WHEN transitioning VERIFY params in transition`() {
        val params = TestParams.Params1
        assertEquals(params, subject.transition(TestState.A, TestEvent.Event1, params)!!.params)
    }
}