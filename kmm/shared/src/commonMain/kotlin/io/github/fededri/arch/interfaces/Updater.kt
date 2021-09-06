package io.github.fededri.arch.interfaces

import io.github.fededri.arch.Next


interface Updater<Action : Any, State : Any, SideEffect : SideEffectInterface, Event : Any> {
    fun onNewAction(action: Action, currentState: State): Next<State, SideEffect, Event>
}