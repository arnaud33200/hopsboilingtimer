package ca.arnaud.hopsboilingtimer.data.repository

import android.util.Log
import ca.arnaud.hopsboilingtimer.data.datasource.PreferencesLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.model.preferences.PatchPreferencesParams
import ca.arnaud.hopsboilingtimer.domain.model.preferences.Preferences
import ca.arnaud.hopsboilingtimer.domain.repository.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesLocalDataSource: PreferencesLocalDataSource,
) : PreferencesRepository {

    private val _preferencesFlow = MutableSharedFlow<Preferences>()

    override suspend fun patchPreferences(params: PatchPreferencesParams): Response<Preferences, Throwable> {
        val currentPreferences = preferencesLocalDataSource.getPreferences() ?: Preferences()
        val newPreferences = currentPreferences.copy(
            darkMode = params.darkMode ?: currentPreferences.darkMode
        )
        preferencesLocalDataSource.setPreferences(newPreferences)
        _preferencesFlow.emit(newPreferences)
        return Response.ofSuccess(newPreferences)
    }

    override fun getPreferencesFlow(): Flow<Preferences> {
        return _preferencesFlow
    }
}