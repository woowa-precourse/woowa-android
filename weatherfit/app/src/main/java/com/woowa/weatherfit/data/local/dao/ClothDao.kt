package com.woowa.weatherfit.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.woowa.weatherfit.data.local.entity.ClothEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothDao {
    @Query("SELECT * FROM clothes ORDER BY createdAt DESC")
    fun getAllClothes(): Flow<List<ClothEntity>>

    @Query("SELECT * FROM clothes WHERE mainCategory = :mainCategory ORDER BY createdAt DESC")
    fun getClothesByMainCategory(mainCategory: String): Flow<List<ClothEntity>>

    @Query("SELECT * FROM clothes WHERE subCategory = :subCategory ORDER BY createdAt DESC")
    fun getClothesBySubCategory(subCategory: String): Flow<List<ClothEntity>>

    @Query("SELECT * FROM clothes WHERE temperatureRange = :temperatureRange ORDER BY createdAt DESC")
    fun getClothesByTemperatureRange(temperatureRange: String): Flow<List<ClothEntity>>

    @Query("SELECT * FROM clothes WHERE id = :id")
    suspend fun getClothById(id: Long): ClothEntity?

    @Query("SELECT * FROM clothes WHERE id IN (:ids)")
    suspend fun getClothesByIds(ids: List<Long>): List<ClothEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCloth(cloth: ClothEntity): Long

    @Update
    suspend fun updateCloth(cloth: ClothEntity)

    @Delete
    suspend fun deleteCloth(cloth: ClothEntity)

    @Query("DELETE FROM clothes WHERE id = :id")
    suspend fun deleteClothById(id: Long)
}
