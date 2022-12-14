package ca.arnaud.hopsboilingtimer.app

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.arnaud.hopsboilingtimer.app.factory.MainScreenModelFactory
import ca.arnaud.hopsboilingtimer.app.mapper.AddNewAdditionParamsMapper
import ca.arnaud.hopsboilingtimer.app.model.*
import ca.arnaud.hopsboilingtimer.app.screen.MainScreenViewModel
import ca.arnaud.hopsboilingtimer.app.service.ClockService
import ca.arnaud.hopsboilingtimer.app.service.PermissionService
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.ScheduleOptions
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.AddNewAddition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.DeleteAddition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.StartAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.StopAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert
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
    private val updateAdditionAlert: UpdateAdditionAlert,
    private val mainScreenModelFactory: MainScreenModelFactory,
    private val addNewAdditionParamsMapper: AddNewAdditionParamsMapper,
    private val clockService: ClockService,
    private val dataStore: DataStore<Preferences>, // TODO - put in a peference use case
    private val permissionService: PermissionService,
) : ViewModel(), MainScreenViewModel {

    companion object {
        const val DARK_THEME_PREFERENCES_KEY = "dark_theme"
    }

    private val darkThemePreferenceKey = booleanPreferencesKey(DARK_THEME_PREFERENCES_KEY)

    private val _screenModel = MutableStateFlow(MainScreenModel())
    override val screenModel: StateFlow<MainScreenModel> = _screenModel

    private val _showRequestPermissionDialog = MutableStateFlow(false)
    override val showRequestPermissionDialog: StateFlow<Boolean> = _showRequestPermissionDialog

    private val _darkMode = MutableStateFlow<Boolean?>(null)
    val darkMode: StateFlow<Boolean?> = _darkMode

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
            dataStore.data.collect { preferences ->
                if (preferences.contains(darkThemePreferenceKey)) {
                    _darkMode.value = preferences[darkThemePreferenceKey]
                }
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
            dataStore.edit { preferences ->
                val darkTheme = preferences[darkThemePreferenceKey] ?: isSystemInDarkTheme
                preferences[darkThemePreferenceKey] = !darkTheme
            }
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