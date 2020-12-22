package com.fedetto.arch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * @param coroutineScope: if null, it will be your ViewModel's viewModelScope,
 * by specifying a custom scope you can dispatch long-running effects independent of your ViewModel lifecycle,
 * but you must handle cancellation
 * @param dispatcher is in charge of deciding in which thread  the coroutine will run.
 * if dispatcher is null the coroutine will use the default dispatcher of the CoroutineScope
 */
interface SideEffectInterface {
    val dispatcher: CoroutineDispatcher?
    val coroutineScope: CoroutineScope?
}