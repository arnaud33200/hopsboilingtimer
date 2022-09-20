package ca.arnaud.hopsboilingtimer.local

import ca.arnaud.hopsboilingtimer.data.datasource.AdditionLocalDataSource
import ca.arnaud.hopsboilingtimer.data.datasource.ScheduleLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.local.factory.AdditionAlertFactory
import ca.arnaud.hopsboilingtimer.local.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.local.mapper.AlertEntityMapper
import ca.arnaud.hopsboilingtimer.local.mapper.ScheduleEntityMapper
import javax.inject.Inject

class ScheduleLocalDataSourceImpl @Inject constructor(
    private val additionLocalDataSource: AdditionLocalDataSource,
    private val appDatabase: AppDatabase,
    private val additionAlertFactory: AdditionAlertFactory,
    private val additionScheduleFactory: AdditionScheduleFactory,
    private val scheduleEntityMapper: ScheduleEntityMapper,
    private val alertEntityMapper: AlertEntityMapper,
) : ScheduleLocalDataSource {

    override suspend fun getSchedule(): AdditionSchedule? {
        return appDatabase.scheduleDao().getSchedules().firstOrNull()?.let { scheduleEntity ->
            val alerts = scheduleEntity.alertIds.mapNotNull { alertId -> getAlert(alertId) }
            additionScheduleFactory.create(scheduleEntity, alerts)
        }
    }

    private suspend fun getAlert(alertId: String): AdditionAlert? {
        return appDatabase.scheduleDao().getAlert(alertId)?.let { alertEntity ->
            // TODO - replace with getAddition(id)
            val additions = additionLocalDataSource.getAdditions().filter { addition ->
                alertEntity.additionIds.contains(addition.id)
            }
            additionAlertFactory.create(alertEntity, additions)
        }
    }

    override suspend fun setSchedule(schedule: AdditionSchedule): Result<AdditionSchedule> {
        return try {
            val scheduleEntity = scheduleEntityMapper.mapTo(schedule)
            appDatabase.scheduleDao().insertSchedule(scheduleEntity)

            schedule.alerts.forEach { additionAlert ->
                val alertEntity = alertEntityMapper.mapTo(additionAlert)
                appDatabase.scheduleDao().insertAlert(alertEntity)
            }
            Result.success(schedule)
        } catch (exception: Throwable) {
            Result.failure(exception)
        }
    }

    override suspend fun deleteSchedule(schedule: AdditionSchedule): Result<Unit> {
        return try {
            appDatabase.scheduleDao().deleteSchedule(schedule.id)
            schedule.alerts.forEach { alert ->
                appDatabase.scheduleDao().deleteAlert(alert.id)
            }
            Result.success(Unit)
        } catch (exception: Throwable) {
            Result.failure(exception)
        }
    }
}