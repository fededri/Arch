package com.example.example

import com.example.arch.Processor
import kotlinx.coroutines.delay

class CounterProcessor : Processor<SideEffect, Action> {

    override suspend fun dispatchSideEffect(effect: SideEffect): Action {
        delay(3000)
        return Action.Reset
    }
}