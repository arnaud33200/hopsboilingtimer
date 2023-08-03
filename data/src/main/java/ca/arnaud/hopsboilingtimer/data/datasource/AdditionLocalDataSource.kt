package ca.arnaud.hopsboilingtimer.data.datasource

import ca.arnaud.hopsboilingtimer.domain.model.Addition

interface AdditionLocalDataSource {

    suspend fun getAdditions(): List<Addition>

    suspend fun insertAddition(addition: Addition)

    suspend fun deleteAddition(additionId: String)
}