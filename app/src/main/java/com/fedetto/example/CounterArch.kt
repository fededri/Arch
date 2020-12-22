package com.fedetto.example

import com.fedetto.arch.SideEffectInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

data class State(val counter: Int = 0)

sealed class Action {
    object Up : Action()
    object Down : Action()
    object Reset : Action()
}

sealed class Event {
    data class LogSomething(val text: String) : Event()
}

sealed class SideEffect(
    //Use IO dispatcher to run side effects
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    //use viewModelScope
    override val coroutineScope: CoroutineScope? = null
) : SideEffectInterface {
    //Set counter to zero
    object ResetEffect : SideEffect()
}