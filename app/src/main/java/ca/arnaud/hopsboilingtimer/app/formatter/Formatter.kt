package ca.arnaud.hopsboilingtimer.app.formatter

interface Formatter<in Input, out Ouput> {

    fun format(input: Input): Ouput
}