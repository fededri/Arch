package io.github.fededri.arch.coroutines

import kotlinx.coroutines.channels.BufferOverflow

data class EventsConfiguration(
    val replays: Int = 0,
    val extraBufferCapacity: Int = 0,
    val backPressureStrategy: BufferOverflow = BufferOverflow.SUSPEND
)