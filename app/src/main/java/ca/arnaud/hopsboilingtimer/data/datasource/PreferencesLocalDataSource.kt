package ca.arnaud.hopsboilingtimer.data.datasource

import ca.arnaud.hopsboilingtimer.domain.model.preferences.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesLocalDataSource {

    fun getPreferences(): Flow<Preferences?>

    suspend fun setPreferences(preferences: Preferences)
}