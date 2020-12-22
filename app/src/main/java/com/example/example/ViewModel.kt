package com.example.example

import com.example.arch.ArchViewModel
import com.example.arch.Processor
import com.example.arch.Updater
import com.example.arch.coroutines.SharedFlowParameters

class ViewModel(
    updater: Updater<Action, State, SideEffect, Event>,
    processor: Processor<SideEffect, Action>
) : ArchViewModel<Action, State, SideEffect, Event>(updater, State(), processor) {

}