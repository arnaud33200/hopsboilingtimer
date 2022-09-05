package ca.arnaud.hopsboilingtimer.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.arnaud.hopsboilingtimer.app.model.MainScreenModel
import ca.arnaud.hopsboilingtimer.app.screen.MainScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), MainScreenViewModel {

    private val _screenModel = MutableStateFlow(MainScreenModel())
    override val screenModel: StateFlow<MainScreenModel> = _screenModel

    // region new addition action

    override fun newAdditionHopsTextChanged(text: String) {
        _screenModel.update {
            it.copy(
                newAdditionRow = it.newAdditionRow.copy(title = text)
            )
        }
    }

    override fun newAdditionDurationTextChanged(text: String) {
        _screenModel.update {
            it.copy(
                newAdditionRow = it.newAdditionRow.copy(duration = text)
            )
        }
    }

    override fun addAdditionClick() {
        // TODO - call use case, if success clear roww
    }

    // endregion

    override fun startTimerButtonClick() {
        // TODO - build alert and schedule
    }

    init {
        viewModelScope.launch {
            // TODO - get additions
        }
    }
}