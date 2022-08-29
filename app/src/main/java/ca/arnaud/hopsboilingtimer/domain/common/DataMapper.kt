package ca.arnaud.hopsboilingtimer.domain.common

fun interface DataMapper<in Input, out Output> {

    fun mapTo(input: Input): Output
}
