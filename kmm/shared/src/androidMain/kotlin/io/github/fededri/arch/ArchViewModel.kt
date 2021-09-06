package io.github.fededri.arch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

@Suppress("EmptyDefaultConstructor")
actual open class ViewModel actual constructor() : ViewModel() {
    protected actual val viewModelScope: CoroutineScope = createViewModelScope()

    public actual override fun onCleared() {
        super.onCleared()

        viewModelScope.cancel()
    }
}