package ca.arnaud.hopsboilingtimer.app

import android.icu.text.CaseMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.arnaud.hopsboilingtimer.app.mapper.AddNewAdditionParamsMapper
import ca.arnaud.hopsboilingtimer.app.mapper.AdditionRowModelMapper
import ca.arnaud.hopsboilingtimer.app.model.*
import ca.arnaud.hopsboilingtimer.app.screen.MainScreenViewModel
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
import java.time.Duration
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAdditions: GetAdditions,
    private val addNewAddition: AddNewAddition,
    private val deleteAddition: DeleteAddition,
    private val startAdditionSchedule: StartAdditionSchedule,
    private val stopAdditionSchedule: StopAdditionSchedule,
    private val subscribeAdditionSchedule: SubscribeAdditionSchedule,
    private val additionRowModelMapper: AdditionRowModelMapper,
    private val addNewAdditionParamsMapper: AddNewAdditionParamsMapper
) : ViewModel(), MainScreenViewModel {

    private val _screenModel = MutableStateFlow(MainScreenModel())
    override val screenModel: StateFlow<MainScreenModel> = _screenModel

    init {
        viewModelScope.launch {
            updateAdditions()
            subscribeAdditionSchedule.execute().collect { schedule ->
                val currentAddNewAddition = screenModel.value.newAdditionRow ?: NewAdditionModel()
                _screenModel.update { model ->
                    // TODO - move stuff into factory
                    model.copy(
                        newAdditionRow = when (schedule) {
                            null -> currentAddNewAddition
                            else -> null
                        },
                        bottomBarModel = when (schedule) {
                            null -> BottomBarModel(
                                buttonTitle = "Start Timer",
                                buttonStyle = ButtonStyle.Start
                            )
                            else -> {
                                BottomBarModel(
                                    buttonTitle = "Stop Timer",
                                    buttonStyle = ButtonStyle.Stop
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    private suspend fun updateAdditions() {
        val result = getAdditions.execute(Unit)
        val additions = result.getOrDefault(emptyList())

        _screenModel.update { model ->
            model.copy(additionRows = additions.map { additionRowModelMapper.mapTo(it) })
        }
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
                    updateAdditions()
                }
            }
        }
    }

    override fun onDeleteAddition(additionRowModel: AdditionRowModel) {
        viewModelScope.launch {
            deleteAddition.execute(DeleteAddition.Params(additionRowModel.id))
            updateAdditions()
        }
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