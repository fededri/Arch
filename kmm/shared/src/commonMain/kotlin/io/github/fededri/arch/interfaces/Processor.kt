package io.github.fededri.arch.interfaces

interface Processor<SideEffect : SideEffectInterface, out Action : Any> {
    suspend fun dispatchSideEffect(effect: SideEffect): Action
}