package ca.arnaud.hopsboilingtimer.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.arnaud.hopsboilingtimer.app.mapper.AdditionRowModelMapper
import ca.arnaud.hopsboilingtimer.app.model.AdditionRowModel
import ca.arnaud.hopsboilingtimer.app.model.BottomBarModel
import ca.arnaud.hopsboilingtimer.app.model.ButtonStyle
import ca.arnaud.hopsboilingtimer.app.model.MainScreenModel
import ca.arnaud.hopsboilingtimer.app.screen.MainScreenViewModel
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.AddNewAddition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.DeleteAddition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.StartAdditionSchedule
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
    private val subscribeAdditionSchedule: SubscribeAdditionSchedule,
    private val additionRowModelMapper: AdditionRowModelMapper,
) : ViewModel(), MainScreenViewModel {

    private val _screenModel = MutableStateFlow(MainScreenModel())
    override val screenModel: StateFlow<MainScreenModel> = _screenModel

    init {
        viewModelScope.launch {
            updateAdditions()
            subscribeAdditionSchedule.execute().collect { schedule ->
                val currentAddNewAddition = screenModel.value.newAdditionRow
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
        _screenModel.update {
            it.copy(
                newAdditionRow = it.newAdditionRow?.copy(title = text)
            )
        }
    }

    override fun newAdditionDurationTextChanged(text: String) {
        _screenModel.update {
            it.copy(
                newAdditionRow = it.newAdditionRow?.copy(duration = text)
            )
        }
    }

    override fun addAdditionClick() {
        viewModelScope.launch {
            val newAddition = screenModel.value.newAdditionRow
            val params = AddNewAddition.Params(
                name = newAddition?.title ?: "",
                duration = Duration.ofMinutes(newAddition?.duration?.toLong() ?: 0L)
            )
            val result = addNewAddition.execute(params)
            when {
                result.isSuccess -> {
                    _screenModel.update {
                        it.copy(newAdditionRow = AdditionRowModel())
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
            startAdditionSchedule.execute()
        }
    }
}