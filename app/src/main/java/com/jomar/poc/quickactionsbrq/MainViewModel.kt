package com.jomar.poc.quickactionsbrq

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var shortcutType by mutableStateOf<ShortcutTypeEnum?>(null)
        private set

    fun onShortcutClicked(type: ShortcutTypeEnum) {
        shortcutType = type
    }
}

