package com.example.arch.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal object DispatcherProvider {
    fun mainDispatcher() : CoroutineDispatcher = Dispatchers.Main
    fun ioDispatcher() : CoroutineDispatcher = Dispatchers.IO
    fun cpuBoundDispatcher() : CoroutineDispatcher = Dispatchers.Default
}