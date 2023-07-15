package ca.arnaud.hopsboilingtimer.domain.usecase.common

interface NoParamsUseCase<out Output> {

    fun execute(): Output
}