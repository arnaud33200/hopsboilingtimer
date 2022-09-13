package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.data.datasource.AdditionLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import javax.inject.Inject

class AdditionRepositoryImpl @Inject constructor(
    private val additionLocalDataSource: AdditionLocalDataSource,
) : AdditionRepository {

    private val additions = mutableListOf<Addition>()

    override suspend fun getAdditions(): Result<List<Addition>> {
        return Result.success(
            additions.takeIf { it.isNotEmpty() }
                ?: additionLocalDataSource.getAdditions().also { localAdditions ->
                    additions.clear()
                    additions.addAll(localAdditions)
                }
        )
    }

    override suspend fun addAddition(addition: Addition): Result<Unit> {
        // TODO - add the addition to the right group
        additions.add(addition)
        additionLocalDataSource.insertAddition(addition)
        return Result.success(Unit)
    }

    override suspend fun deleteAddition(additionId: String): Result<Unit> {
        additionLocalDataSource.deleteAddition(additionId)
        additions.removeIf { it.id == additionId }
        return Result.success(Unit)
    }
}