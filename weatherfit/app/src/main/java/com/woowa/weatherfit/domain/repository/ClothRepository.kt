package com.woowa.weatherfit.domain.repository

import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.model.TemperatureRange
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ClothRepository {
    // Local DB methods
    fun getAllClothes(): Flow<List<Cloth>>

    fun getClothesByMainCategory(mainCategory: MainCategory): Flow<List<Cloth>>

    fun getClothesBySubCategory(subCategory: SubCategory): Flow<List<Cloth>>

    fun getClothesByTemperatureRange(temperatureRange: TemperatureRange): Flow<List<Cloth>>

    suspend fun getClothById(id: Long): Cloth?

    suspend fun insertCloth(cloth: Cloth): Long

    suspend fun updateCloth(cloth: Cloth)

    suspend fun deleteCloth(cloth: Cloth)

    suspend fun deleteClothById(id: Long)

    // Server API methods
    suspend fun registerClothesToServer(
        imageFile: File,
        category: MainCategory,
        subCategory: SubCategory
    ): Result<Cloth>

    suspend fun updateClothesOnServer(
        clothesId: Long,
        category: MainCategory,
        subCategory: SubCategory
    ): Result<Cloth>

    suspend fun deleteClothesFromServer(clothesId: Long): Result<Unit>

    suspend fun fetchClothesDetailFromServer(clothesId: Long): Result<Cloth>

    suspend fun fetchClothesListFromServer(
        category: MainCategory? = null,
        sub: SubCategory? = null,
        cursor: Long? = null,
        size: Int = 20
    ): Result<List<Cloth>>
}
