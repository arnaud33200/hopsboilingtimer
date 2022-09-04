package ca.arnaud.hopsboilingtimer.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.arnaud.hopsboilingtimer.app.model.MainScreenModel
import ca.arnaud.hopsboilingtimer.app.screen.MainScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), MainScreenViewModel {

    private val _screenModel = MutableStateFlow(MainScreenModel())
    override val screenModel: StateFlow<MainScreenModel> = _screenModel

    init {
        viewModelScope.launch {
            // TODO - get additions
        }
    }
}