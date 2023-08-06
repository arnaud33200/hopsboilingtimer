package ca.arnaud.hopsboilingtimer.app.feature.additiontimer

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.factory.AdditionTimerScreenModelFactory
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.mapper.AddNewAdditionParamsMapper
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionOptionType
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionRowModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionTimerScreenModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.NewAdditionModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.TimerTextUpdateModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.screen.AdditionTimerScreenActionListener
import ca.arnaud.hopsboilingtimer.app.service.ClockService
import ca.arnaud.hopsboilingtimer.app.service.PermissionService
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.preferences.PatchPreferencesParams
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleOptions
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.model.schedule.getSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.AddNewAddition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.DeleteAddition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import ca.arnaud.hopsboilingtimer.domain.usecase.preferences.PatchPreferences
import ca.arnaud.hopsboilingtimer.domain.usecase.preferences.SubscribePreferences
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.StartAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.StopAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeNextAdditionAlert
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
    private val subscribeNextAdditionAlert: SubscribeNextAdditionAlert,
    private val updateAdditionAlert: UpdateAdditionAlert,
    private val patchPreferences: PatchPreferences,
    private val subscribePreferences: SubscribePreferences,
    private val additionTimerScreenModelFactory: AdditionTimerScreenModelFactory,
    private val addNewAdditionParamsMapper: AddNewAdditionParamsMapper,
    private val clockService: ClockService,
    private val permissionService: PermissionService,
) : ViewModel(), AdditionTimerScreenActionListener {

    private val _screenModel = MutableStateFlow<AdditionTimerScreenModel>(
        AdditionTimerScreenModel.Loading
    )
    val screenModel: StateFlow<AdditionTimerScreenModel> = _screenModel

    private val _timerTextUpdate = MutableStateFlow(TimerTextUpdateModel())
    val timerTextUpdate: StateFlow<TimerTextUpdateModel> = _timerTextUpdate

    private val _newAddition = MutableStateFlow(NewAdditionModel())
    val newAddition: StateFlow<NewAdditionModel> = _newAddition

    private val _showRequestPermissionDialog = MutableStateFlow(false)
    val showRequestPermissionDialog: StateFlow<Boolean> = _showRequestPermissionDialog


    private var scheduleStatus: ScheduleStatus? = null
    private val currentSchedule get() = scheduleStatus?.getSchedule()

    data class AdditionClockEvent(
        val scheduleStatus: ScheduleStatus,
        val nextAlert: AdditionAlert?,
    )

    init {
        viewModelScope.launch {
            val scheduleFlow = subscribeAdditionSchedule.execute()
            val nextAlertFlow = subscribeNextAdditionAlert.execute()

            subscribeSchedule(scheduleFlow)
            subscribeNextAlert(nextAlertFlow)
            subscribeClockService(scheduleFlow, nextAlertFlow)
        }
    }

    private fun onClockTick(event: AdditionClockEvent) {
        val schedule = event.scheduleStatus.getSchedule() ?: return
        val nextAlert = event.nextAlert ?: return
        _timerTextUpdate.value = TimerTextUpdateModel(
            buttonTimer = additionTimerScreenModelFactory.getButtonTime(schedule),
            highlightRowTimer = additionTimerScreenModelFactory.getHighlightedTimeText(nextAlert)
        )
    }

    private suspend fun updateScreenModel() {
        val result = getAdditions.execute(Unit)
        val additions = result.getOrDefault(emptyList())
        _screenModel.value = additionTimerScreenModelFactory.create(
            additions, currentSchedule,
        )
    }

    // region Subscription

    private fun subscribeSchedule(scheduleFlow: Flow<ScheduleStatus>) {
        viewModelScope.launch {
            scheduleFlow.collect { status ->
                onScheduleUpdated(status)
            }
        }
    }

    private fun subscribeNextAlert(nextAlertFlow: Flow<AdditionAlert?>) {
        viewModelScope.launch {
            nextAlertFlow.collect { additionAlert ->
                if (additionAlert != null) {
                    updateScreenModel()
                }
            }
        }
    }

    private fun subscribeClockService(
        scheduleFlow: Flow<ScheduleStatus>,
        nextAlertFlow: Flow<AdditionAlert?>,
    ) {
        viewModelScope.launch {
            combine(
                clockService.getTickFlow(),
                scheduleFlow,
                nextAlertFlow
            ) { tick, status, nextAddition ->
                AdditionClockEvent(status, nextAddition)
            }.collect { event ->
                onClockTick(event)
            }
        }
    }

    // endregion

    // region Schedule

    private suspend fun onScheduleUpdated(status: ScheduleStatus) {
        if (status == scheduleStatus && screenModel.value !is AdditionTimerScreenModel.Loading) {
            return
        }

        scheduleStatus = status
        when (status) {
            is ScheduleStatus.Started -> clockService.start()
            ScheduleStatus.Iddle,
            ScheduleStatus.Canceled,
            ScheduleStatus.Stopped -> {
                clockService.reset()
                _timerTextUpdate.value = TimerTextUpdateModel()
            }
        }
        updateScreenModel()
    }

    private suspend fun startSchedule() {
        if (!permissionService.hasNotificationPermission()) {
            showPermissionDialog()
            return
        }

        if (!permissionService.willNotificationBeVisible()) {
            // TODO - show warning, either a toast or a message at the bottom bar
        }

        startAdditionSchedule.execute(ScheduleOptions())
    }

    private suspend fun stopSchedule() {
        // TODO - instead of calling onScheduleUpdated, better to have a "Stopping" state
        //  show a loader on the button and stop the timer
        onScheduleUpdated(ScheduleStatus.Canceled)
        stopAdditionSchedule.execute(Unit)
    }

    // endregion

    // region new addition action

    override fun newAdditionHopsTextChanged(text: String) {
        updateNewAdditionModel(
            title = text,
            duration = newAddition.value.duration
        )
    }

    override fun newAdditionDurationTextChanged(text: String) {
        updateNewAdditionModel(
            title = newAddition.value.title,
            duration = text
        )
    }

    private fun updateNewAdditionModel(title: String, duration: String) {
        val enabled = title.isNotBlank() && duration.isNotBlank()
        _newAddition.update { newAddition ->
            newAddition.copy(
                title = title,
                duration = duration,
                buttonEnabled = enabled
            )
        }
    }

    override fun addAdditionClick() {
        viewModelScope.launch {
            val params = addNewAdditionParamsMapper.mapTo(newAddition.value)
            val result = addNewAddition.execute(params)
            when {
                result.isSuccess -> {
                    updateNewAdditionModel(title = "", duration = "")
                    updateScreenModel()
                }
            }
        }
    }

    override fun onAdditionRowOptionClick(
        rowModel: AdditionRowModel,
        optionType: AdditionOptionType
    ) {
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

    override fun onThemeIconClick() {
        viewModelScope.launch {
            val preferences = subscribePreferences.execute().first()
            patchPreferences.execute(
                params = PatchPreferencesParams(
                    darkMode = !(preferences.darkMode ?: false)
                )
            )
        }
    }

    private suspend fun deleteAddition(rowModel: AdditionRowModel) {
        deleteAddition.execute(DeleteAddition.Params(rowModel.id))
        updateScreenModel()
    }

    // endregion

    override fun startTimerButtonClick() {
        viewModelScope.launch {
            when (subscribeAdditionSchedule.execute().first()) {
                is ScheduleStatus.Started -> stopSchedule()
                ScheduleStatus.Iddle,
                ScheduleStatus.Canceled,
                ScheduleStatus.Stopped -> startSchedule()
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