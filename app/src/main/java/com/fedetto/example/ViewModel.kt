package com.fedetto.example

import com.fedetto.arch.ArchViewModel
import com.fedetto.arch.Processor
import com.fedetto.arch.Updater

class ViewModel(
    updater: Updater<Action, State, SideEffect, Event>,
    processor: Processor<SideEffect, Action>
) : ArchViewModel<Action, State, SideEffect, Event>(updater, State(), processor) {

}