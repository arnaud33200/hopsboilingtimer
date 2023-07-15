package ca.arnaud.hopsboilingtimer.app.feature.additiontimer

import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.factory.AdditionTimerScreenModelFactory
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.mapper.AddNewAdditionParamsMapper
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionOptionType
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.ButtonStyle
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.MainScreenModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.NewAdditionModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.RowModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.screen.AdditionTimerScreenActionListener
import ca.arnaud.hopsboilingtimer.app.service.ClockService
import ca.arnaud.hopsboilingtimer.app.service.PermissionService
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.ScheduleOptions
import ca.arnaud.hopsboilingtimer.domain.model.preferences.PatchPreferencesParams
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.AddNewAddition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.DeleteAddition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import ca.arnaud.hopsboilingtimer.domain.usecase.preferences.PatchPreferences
import ca.arnaud.hopsboilingtimer.domain.usecase.preferences.SubscribePreferences
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.StartAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.StopAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdditionTimerViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted private val bundle: Bundle?,
    private val getAdditions: GetAdditions,
    private val addNewAddition: AddNewAddition,
    private val deleteAddition: DeleteAddition,
    private val startAdditionSchedule: StartAdditionSchedule,
    private val stopAdditionSchedule: StopAdditionSchedule,
    private val subscribeAdditionSchedule: SubscribeAdditionSchedule,
    private val updateAdditionAlert: UpdateAdditionAlert,
    private val patchPreferences: PatchPreferences,
    private val subscribePreferences: SubscribePreferences,
    private val additionTimerScreenModelFactory: AdditionTimerScreenModelFactory,
    private val addNewAdditionParamsMapper: AddNewAdditionParamsMapper,
    private val clockService: ClockService,
    private val permissionService: PermissionService,
) : ViewModel(), AdditionTimerScreenActionListener {

    private val _screenModel = MutableStateFlow(MainScreenModel())
    val screenModel: StateFlow<MainScreenModel> = _screenModel

    private val _showRequestPermissionDialog = MutableStateFlow(false)
    val showRequestPermissionDialog: StateFlow<Boolean> = _showRequestPermissionDialog

    private var darkMode: Boolean? = null

    private var currentSchedule: AdditionSchedule? = null

    init {
        viewModelScope.launch {
            subscribeAdditionSchedule.execute().collect { schedule ->
                currentSchedule = schedule
                when (schedule) {
                    null -> {
                        clockService.reset()
                    }

                    else -> clockService.start()
                }
                updateScreenModel()
            }
        }

        viewModelScope.launch {
            clockService.getTickFlow().collect { tick ->
                updateScreenModel()
            }
        }

        viewModelScope.launch {
            subscribePreferences.execute().collect { preferences ->
                darkMode = preferences.darkMode
            }
        }
    }

    private suspend fun updateScreenModel() {
        val result = getAdditions.execute(Unit)
        val additions = result.getOrDefault(emptyList())
        val currentAddNewAddition = screenModel.value.newAdditionRow ?: NewAdditionModel()
        _screenModel.value = additionTimerScreenModelFactory.create(
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

    override fun onOptionClick(rowModel: RowModel, optionType: AdditionOptionType) {
        viewModelScope.launch {
            when (optionType) {
                AdditionOptionType.Delete -> deleteAddition(rowModel)
            }
        }
    }

    override fun onAlertRowCheckChanged(checked: Boolean, alertId: String) {
        viewModelScope.launch {
            val params = UpdateAdditionAlert.Params(
                alertId = alertId,
                checked = checked
            )
            updateAdditionAlert.execute(params)
        }
    }

    override fun onThemeIconClick(isSystemInDarkTheme: Boolean) {
        viewModelScope.launch {
            patchPreferences.execute(
                params = PatchPreferencesParams(
                    darkMode = !(darkMode ?: false)
                )
            )
        }
    }

    private suspend fun deleteAddition(rowModel: RowModel) {
        when (rowModel) {
            is RowModel.AdditionRowModel -> {
                deleteAddition.execute(DeleteAddition.Params(rowModel.id))
                updateScreenModel()
            }

            else -> {
                // No-op
            }
        }
    }

    // endregion

    override fun startTimerButtonClick() {
        // TODO - get Delay and reset it
        viewModelScope.launch {
            when (screenModel.value.bottomBarModel.buttonStyle) {
                ButtonStyle.Start -> {
                    if (!permissionService.hasNotificationPermission()) {
                        showPermissionDialog()
                        return@launch
                    }
                    startAdditionSchedule.execute(ScheduleOptions())
                }

                ButtonStyle.Stop -> stopAdditionSchedule.execute()
            }

        }
    }

    private fun showPermissionDialog() {
        _showRequestPermissionDialog.value = true
    }

    override fun onSubButtonClick() {

    }

    override fun onPermissionResult(granted: Boolean) {
        _showRequestPermissionDialog.value = false
        if (granted) {
            startTimerButtonClick()
        }
    }
}