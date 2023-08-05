package ca.arnaud.hopsboilingtimer.domain.common

fun interface SuspendDataMapper<in Input, out Output> {

    suspend fun mapTo(input: Input): Output
}
