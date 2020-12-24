package com.fedetto.arch.interfaces

interface ActionsDispatcher<Action : Any> {
    fun action(action: Action)
}