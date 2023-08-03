package ca.arnaud.hopsboilingtimer.domain.common

interface TwoWayMapper<Input, Output> {

    fun mapTo(input: Input): Output

    fun mapFrom(output: Output): Input
}