package ca.arnaud.hopsboilingtimer.domain.common

sealed interface Response<Data, Error : Throwable> {

    companion object {
        fun <Data, Error : Throwable> ofSuccess(data: Data) = Success<Data, Error>(data)
        fun <Data, Error : Throwable> ofFailure(error: Error) = Failure<Data, Error>(error)
    }

    data class Success<Data, Error : Throwable>(
        val data: Data,
    ) : Response<Data, Error>

    data class Failure<Data, Error : Throwable>(
        val error: Error,
    ) : Response<Data, Error>
}

fun <Data, Error : Throwable> Response<Data, Error>.doOnSuccess(callback: (Data) -> Unit) {
    when (this) {
        is Response.Success -> callback(this.data)
        is Response.Failure -> { } // No-op
    }
}