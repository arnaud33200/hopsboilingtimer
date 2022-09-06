package ca.arnaud.hopsboilingtimer.domain.common

sealed interface Result<Data> {

    data class Success<Data>(
        val data: Data
    ): Result<Data>

    data class Failure<Data>(
        val error: Throwable
    ): Result<Data>
}