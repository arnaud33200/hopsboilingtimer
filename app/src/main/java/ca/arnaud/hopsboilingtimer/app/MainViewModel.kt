package ca.arnaud.hopsboilingtimer.app

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.arnaud.hopsboilingtimer.domain.usecase.preferences.SubscribePreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted private val bundle: Bundle?,
    subscribePreferences: SubscribePreferences,
) : ViewModel() {

    private val _darkMode = MutableStateFlow<Boolean?>(null)
    val darkMode: StateFlow<Boolean?> = _darkMode

    init {
        viewModelScope.launch {
            subscribePreferences.execute().collect { preferences ->
                _darkMode.value = preferences.darkMode
            }
        }
    }
}