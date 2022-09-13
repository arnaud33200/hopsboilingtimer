package ca.arnaud.hopsboilingtimer.domain.repository

import ca.arnaud.hopsboilingtimer.domain.model.Addition

interface AdditionRepository {

    suspend fun getAdditions(): Result<List<Addition>>

    suspend fun addAddition(addition: Addition): Result<Unit>

    suspend fun deleteAddition(additionId: String): Result<Unit>
}