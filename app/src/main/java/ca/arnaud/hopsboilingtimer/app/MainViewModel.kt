package ca.arnaud.hopsboilingtimer.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.arnaud.hopsboilingtimer.app.factory.MainScreenModelFactory
import ca.arnaud.hopsboilingtimer.app.mapper.AddNewAdditionParamsMapper
import ca.arnaud.hopsboilingtimer.app.model.*
import ca.arnaud.hopsboilingtimer.app.screen.MainScreenViewModel
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.AddNewAddition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.DeleteAddition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.StartAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.StopAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeAdditionSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAdditions: GetAdditions,
    private val addNewAddition: AddNewAddition,
    private val deleteAddition: DeleteAddition,
    private val startAdditionSchedule: StartAdditionSchedule,
    private val stopAdditionSchedule: StopAdditionSchedule,
    private val subscribeAdditionSchedule: SubscribeAdditionSchedule,
    private val mainScreenModelFactory: MainScreenModelFactory,
    private val addNewAdditionParamsMapper: AddNewAdditionParamsMapper,
) : ViewModel(), MainScreenViewModel {

    private val _screenModel = MutableStateFlow(MainScreenModel())
    override val screenModel: StateFlow<MainScreenModel> = _screenModel

    private var currentSchedule: AdditionSchedule? = null

    init {
        viewModelScope.launch {
            subscribeAdditionSchedule.execute().collect { schedule ->
                currentSchedule = schedule
                updateScreenModel()
            }
        }
    }

    private suspend fun updateScreenModel() {
        val result = getAdditions.execute(Unit)
        val additions = result.getOrDefault(emptyList())
        val currentAddNewAddition = screenModel.value.newAdditionRow ?: NewAdditionModel()
        _screenModel.value = mainScreenModelFactory.create(
            additions, currentSchedule, currentAddNewAddition
        )
    }

    // region new addition action

    override fun newAdditionHopsTextChanged(text: String) {
        val newAddition = screenModel.value.newAdditionRow ?: return
        updateNewAdditionModel(
            title = text,
            duration = newAddition.duration
        )
    }

    override fun newAdditionDurationTextChanged(text: String) {
        val newAddition = screenModel.value.newAdditionRow ?: return
        updateNewAdditionModel(
            title = newAddition.title,
            duration = text
        )
    }

    private fun updateNewAdditionModel(title: String, duration: String) {
        val newAddition = screenModel.value.newAdditionRow ?: return
        _screenModel.update {
            // TODO - setup validator & formatter
            val enabled = title.isNotBlank() && duration.isNotBlank()
            it.copy(
                newAdditionRow = newAddition.copy(
                    title = title,
                    duration = duration,
                    buttonEnabled = enabled
                )
            )
        }
    }

    override fun addAdditionClick() {
        val newAddition = screenModel.value.newAdditionRow ?: return
        viewModelScope.launch {
            val params = addNewAdditionParamsMapper.mapTo(newAddition)
            val result = addNewAddition.execute(params)
            when {
                result.isSuccess -> {
                    _screenModel.update {
                        it.copy(newAdditionRow = NewAdditionModel())
                    }
                    updateScreenModel()
                }
            }
        }
    }

    override fun onOptionClick(additionRowModel: AdditionRowModel, optionType: AdditionOptionType) {
        viewModelScope.launch {
            when (optionType) {
                AdditionOptionType.Delete -> deleteAddition(additionRowModel)
            }
        }
    }

    private suspend fun deleteAddition(additionRowModel: AdditionRowModel) {
        deleteAddition.execute(DeleteAddition.Params(additionRowModel.id))
        updateScreenModel()
    }

    // endregion

    override fun startTimerButtonClick() {
        viewModelScope.launch {
            when (screenModel.value.bottomBarModel.buttonStyle) {
                ButtonStyle.Start -> startAdditionSchedule.execute()
                ButtonStyle.Stop -> stopAdditionSchedule.execute()
            }

        }
    }
}