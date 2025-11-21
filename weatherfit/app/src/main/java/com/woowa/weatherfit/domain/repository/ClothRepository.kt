package com.woowa.weatherfit.domain.repository

import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.model.TemperatureRange
import kotlinx.coroutines.flow.Flow

interface ClothRepository {
    fun getAllClothes(): Flow<List<Cloth>>

    fun getClothesByMainCategory(mainCategory: MainCategory): Flow<List<Cloth>>

    fun getClothesBySubCategory(subCategory: SubCategory): Flow<List<Cloth>>

    fun getClothesByTemperatureRange(temperatureRange: TemperatureRange): Flow<List<Cloth>>

    suspend fun getClothById(id: Long): Cloth?

    suspend fun insertCloth(cloth: Cloth): Long

    suspend fun updateCloth(cloth: Cloth)

    suspend fun deleteCloth(cloth: Cloth)

    suspend fun deleteClothById(id: Long)
}
