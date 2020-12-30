package com.fedetto.arch

import com.fedetto.arch.interfaces.SideEffectInterface

sealed class Next<out State : Any, out SideEffect : SideEffectInterface, out Event : Any> {

    abstract val state: State
    open val sideEffects: Set<SideEffect> = emptySet()
    open val events: Set<Event> = emptySet()


    data class State<out State : Any>(override val state: State) : Next<State, Nothing, Nothing>()

    data class StateWithSideEffects<out State : Any, out SideEffect : SideEffectInterface>(
        override val state: State,
        override val sideEffects:  Set<SideEffect>
    ) : Next<State, SideEffect, Nothing>()

    data class StateWithSideEffectsAndEvents<out State : Any, out SideEffect : SideEffectInterface, out Event : Any>(
        override val state: State,
        override val sideEffects: Set<SideEffect>,
        override val events: Set<Event>
    ) : Next<State, SideEffect, Event>()

    data class StateWithEvents<out State : Any, out Event : Any>(
        override val state: State,
        override val events:  Set<Event>
    ) : Next<State, Nothing, Event>()
}