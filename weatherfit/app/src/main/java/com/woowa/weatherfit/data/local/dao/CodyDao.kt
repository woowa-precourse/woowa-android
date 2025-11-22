package com.woowa.weatherfit.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.woowa.weatherfit.data.local.entity.CodyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CodyDao {
    @Query("SELECT * FROM codies ORDER BY createdAt DESC")
    fun getAllCodies(): Flow<List<CodyEntity>>

    @Query("SELECT * FROM codies WHERE category = :season ORDER BY createdAt DESC")
    fun getCodiesBySeason(season: String): Flow<List<CodyEntity>>

    @Query("SELECT * FROM codies WHERE id = :id")
    suspend fun getCodyById(id: Long): CodyEntity?

    @Query("SELECT * FROM codies WHERE id = :id")
    fun getCodyByIdFlow(id: Long): Flow<CodyEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCody(cody: CodyEntity): Long

    @Update
    suspend fun updateCody(cody: CodyEntity)

    @Delete
    suspend fun deleteCody(cody: CodyEntity)

    @Query("DELETE FROM codies WHERE id = :id")
    suspend fun deleteCodyById(id: Long)

    @Query("DELETE FROM codies")
    suspend fun deleteAll()
}
