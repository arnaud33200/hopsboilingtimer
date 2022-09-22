package ca.arnaud.hopsboilingtimer.app

import android.app.Application
import ca.arnaud.hopsboilingtimer.app.alarm.AdditionAlarmScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HopsBoilingTimerApplication : Application() {

    @Inject
    lateinit var additionAlarmScheduler: AdditionAlarmScheduler
}
