package ca.arnaud.hopsboilingtimer.data.datasource

import ca.arnaud.hopsboilingtimer.domain.model.preferences.Preferences

interface PreferencesLocalDataSource {

    suspend fun getPreferences(): Preferences?

    suspend fun setPreferences(preferences: Preferences)
}