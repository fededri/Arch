package io.github.fededri.arch

import io.github.fededri.arch.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
var createViewModelScope: () -> CoroutineScope = {
    CoroutineScope(DispatcherProvider.mainDispatcher() + SupervisorJob())
}