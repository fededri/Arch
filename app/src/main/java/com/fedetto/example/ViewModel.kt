package com.fedetto.example

import com.fedetto.arch.ArchViewModel
import com.fedetto.arch.interfaces.Processor
import com.fedetto.arch.interfaces.Updater

class ViewModel(
    updater: Updater<Action, State, SideEffect, Event>,
    processor: Processor<SideEffect, Action>
) : ArchViewModel<Action, State, SideEffect, Event, Nothing>(
    updater,
    State(),
    setOf(SideEffect.ResetEffect),
    processor
) {

}