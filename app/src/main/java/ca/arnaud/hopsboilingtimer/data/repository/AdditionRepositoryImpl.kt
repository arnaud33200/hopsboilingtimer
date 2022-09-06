package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import javax.inject.Inject

class AdditionRepositoryImpl @Inject constructor() : AdditionRepository {

    private val additions = mutableListOf<Addition>()

    override suspend fun getAdditions(): Result<List<Addition>> {
        // TODO - call local data source
        return Result.success(additions)
    }

    override suspend fun addAddition(addition: Addition): Result<Unit> {
        // TODO - add the addition to the right group
        additions.add(addition)
        return Result.success(Unit)
    }
}