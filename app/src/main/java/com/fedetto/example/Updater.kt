package com.fedetto.example

import com.fedetto.arch.Next
import com.fedetto.arch.interfaces.Updater

class Updater : Updater<Action, State, SideEffect, Event> {

    override fun onNewAction(action: Action, currentState: State): Next<State, SideEffect, Event> {
        return when (action) {
            Action.Up -> changeCounter(currentState, true)
            Action.Down -> changeCounter(currentState, false)
            Action.Reset -> Next.State(currentState.copy(counter = 0))
        }
    }

    private fun changeCounter(
        currentState: State,
        isIncrement: Boolean
    ): Next<State, SideEffect, Event> {
        val next = when (isIncrement) {
            true -> currentState.counter + 1
            else -> currentState.counter - 1
        }

        return if ((next % 10) == 0) {
            Next.StateWithSideEffectsAndEvents(
                currentState.copy(counter = next),
                setOf(SideEffect.ResetEffect),
                setOf(Event.LogSomething("Multiple of 10"))
            )
        } else {
            Next.State(currentState.copy(counter = next))
        }
    }
}