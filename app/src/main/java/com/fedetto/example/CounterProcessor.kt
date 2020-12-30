package com.fedetto.example

import com.fedetto.arch.interfaces.Processor
import kotlinx.coroutines.delay

class CounterProcessor : Processor<SideEffect, Action> {

    override suspend fun dispatchSideEffect(effect: SideEffect): Action {
        delay(3000)
        return Action.Reset
    }
}