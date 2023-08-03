package ca.arnaud.hopsboilingtimer.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ca.arnaud.hopsboilingtimer.local.entity.AdditionEntity

@Dao
interface AdditionDao {

    @Query("SELECT * FROM additionentity")
    fun getAdditions(): List<AdditionEntity>

    @Query("SELECT * FROM additionentity WHERE id = :additionId")
    fun getAddition(additionId: String): AdditionEntity?

    @Insert
    fun insert(additionEntity: AdditionEntity)

    @Query("DELETE FROM additionentity WHERE id = :additionId")
    fun deleteAddition(additionId: String)
}