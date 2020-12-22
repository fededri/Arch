package com.fedetto.arch.coroutines

import kotlinx.coroutines.channels.BufferOverflow

data class SharedFlowParameters(
    val replays: Int = 0,
    val extraBufferCapacity: Int = 0,
    val backPressureStrategy: BufferOverflow = BufferOverflow.SUSPEND
)