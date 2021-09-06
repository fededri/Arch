package io.github.fededri.arch.interfaces

interface ActionsDispatcher<Action : Any> {
    fun action(action: Action)
}