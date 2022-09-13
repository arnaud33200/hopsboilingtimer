package ca.arnaud.hopsboilingtimer.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ca.arnaud.hopsboilingtimer.local.entity.AdditionEntity

@Dao
interface AdditionDao {

    @Query("SELECT * FROM additionentity")
    fun getAdditions(): List<AdditionEntity>

    @Insert
    fun insert(additionEntity: AdditionEntity)
}