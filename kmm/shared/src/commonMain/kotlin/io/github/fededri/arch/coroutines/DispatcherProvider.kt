package io.github.fededri.arch.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal object DispatcherProvider {
    fun mainDispatcher() : CoroutineDispatcher = Dispatchers.Main
    fun backgroundDispatcher() : CoroutineDispatcher = Dispatchers.Default
}