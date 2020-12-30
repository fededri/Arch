package com.fedetto.arch

import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fedetto.arch.coroutines.DispatcherProvider
import com.fedetto.arch.coroutines.EventsConfiguration
import com.fedetto.arch.interfaces.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext


abstract class ArchViewModel<Action : Any, State : Any, SideEffect : SideEffectInterface, Event : Any, RenderState : Any>
constructor(
    private val updater: Updater<Action, State, SideEffect, Event>,
    initialState: State,
    initialEffects: Set<SideEffect>? = null,
    private val processor: Processor<SideEffect, Action>,
    private val stateMapper: StateMapper<State, RenderState>? = null,
    eventsConfiguration: EventsConfiguration = EventsConfiguration(),
    private val coroutineExceptionHandler: CoroutineExceptionHandler? = null,

    ) : ViewModel(), ActionsDispatcher<Action> {

    private val state = MutableStateFlow(initialState)
    private val events = MutableSharedFlow<Event>(
        eventsConfiguration.replays,
        eventsConfiguration.extraBufferCapacity,
        eventsConfiguration.backPressureStrategy
    )
    private val renderState by lazy { MutableStateFlow(stateMapper?.mapToRenderState(state.value)) }

    init {
        if (initialEffects != null) {
            dispatchSideEffects(initialEffects)
        }
    }

    fun observeState(): Flow<State> {
        return state.asStateFlow()
    }

    fun observeRenderState(): Flow<RenderState?> {
        check(stateMapper != null) {
            "In order to observe a RenderState, first you must pass a state mapper into the Arch's constructor"
        }
        return renderState.asStateFlow()
    }

    fun observeEvents(): Flow<Event> {
        return events.asSharedFlow()
    }

    override fun action(action: Action) {
        check(isOnMainThread(Thread.currentThread())) {
            "Actions must be dispatched from the UI thread"
        }
        val next = updater.onNewAction(action, state.value)
        if (!next.sideEffects.isNullOrEmpty()) {
            dispatchSideEffects(next.sideEffects)
        }

        if (!next.events.isNullOrEmpty()) {
            viewModelScope.launch {
                next.events.forEach {
                    events.emit(it)
                }
            }
        }
        state.value = next.state
        renderState.value = stateMapper?.mapToRenderState(next.state)
    }

    private fun dispatchSideEffects(sideEffects: Set<SideEffect>) {
        sideEffects.forEach { effect ->
            val coroutineScope =
                if (effect.coroutineScope != null) effect.coroutineScope!! else viewModelScope
            val dispatcher =
                if (effect.dispatcher != null) effect.dispatcher!! else coroutineScope.coroutineContext

            val coroutineContext = coroutineExceptionHandler ?: EmptyCoroutineContext
            coroutineScope.launch(coroutineContext) {
                withContext(dispatcher) {
                    val effectAction = processor.dispatchSideEffect(effect)
                    withContext(DispatcherProvider.mainDispatcher()) {
                        action(effectAction)
                    }
                }
            }
        }
    }

    private fun isOnMainThread(thread: Thread): Boolean {
        return thread == Looper.getMainLooper().thread
    }
}