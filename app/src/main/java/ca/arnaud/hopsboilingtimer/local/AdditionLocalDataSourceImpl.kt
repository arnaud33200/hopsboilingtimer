package ca.arnaud.hopsboilingtimer.local

import ca.arnaud.hopsboilingtimer.data.datasource.AdditionLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import javax.inject.Inject

class AdditionLocalDataSourceImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val additionEntityMapper: AdditionEntityMapper,
) : AdditionLocalDataSource {

    override suspend fun getAdditions(): List<Addition> {
        return appDatabase.additionDao().getAdditions().map { entity ->
            additionEntityMapper.mapFrom(entity)
        }
    }

    override suspend fun insertAddition(addition: Addition) {
        appDatabase.additionDao().insert(additionEntityMapper.mapTo(addition))
    }
}