package com.fedetto.arch

import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fedetto.arch.coroutines.DispatcherProvider
import com.fedetto.arch.coroutines.SharedFlowParameters
import com.fedetto.arch.interfaces.ActionsDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext


abstract class ArchViewModel<Action : Any, State : Any, SideEffect : SideEffectInterface, Event : Any>
constructor(
    private val updater: Updater<Action, State, SideEffect, Event>,
    initialState: State,
    private val processor: Processor<SideEffect, Action>,
    eventsFlowParams: SharedFlowParameters = SharedFlowParameters(),
    private val coroutineExceptionHandler: CoroutineExceptionHandler? = null,

) : ViewModel(), ActionsDispatcher<Action> {

    private val state = MutableStateFlow(initialState)
    private val events = MutableSharedFlow<Event>(
        eventsFlowParams.replays,
        eventsFlowParams.extraBufferCapacity,
        eventsFlowParams.backPressureStrategy
    )

    fun observeState(): Flow<State> {
        return state.asStateFlow()
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