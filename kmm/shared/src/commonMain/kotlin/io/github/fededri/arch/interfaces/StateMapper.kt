package io.github.fededri.arch.interfaces

interface StateMapper<State : Any, RenderState : Any> {
    fun mapToRenderState(state: State): RenderState
}