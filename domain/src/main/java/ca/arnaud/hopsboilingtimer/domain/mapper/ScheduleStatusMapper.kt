package ca.arnaud.hopsboilingtimer.domain.mapper

import ca.arnaud.hopsboilingtimer.domain.common.SuspendDataMapper
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleState
import javax.inject.Inject


class ScheduleStatusMapper @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
) : SuspendDataMapper<AdditionScheduleState, ScheduleStatus> {

    sealed class ScheduleStatusMapperError : Throwable() {

        object MissingSchedule : ScheduleStatusMapperError()
    }

    @Throws(ScheduleStatusMapperError::class)
    override suspend fun mapTo(input: AdditionScheduleState): ScheduleStatus {
        return when (input) {
            AdditionScheduleState.Idle -> ScheduleStatus.Iddle
            AdditionScheduleState.Stopped -> ScheduleStatus.Stopped
            AdditionScheduleState.Canceled -> ScheduleStatus.Canceled
            AdditionScheduleState.Started -> ScheduleStatus.Started(
                schedule = scheduleRepository.getSchedule()
                    ?: throw ScheduleStatusMapperError.MissingSchedule
            )
        }
    }
}