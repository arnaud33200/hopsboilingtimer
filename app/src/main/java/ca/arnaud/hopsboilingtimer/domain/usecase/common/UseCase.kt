package ca.arnaud.hopsboilingtimer.domain.usecase.common

interface UseCase<in Params, out Output> {

    fun execute(params: Params): Output
}
