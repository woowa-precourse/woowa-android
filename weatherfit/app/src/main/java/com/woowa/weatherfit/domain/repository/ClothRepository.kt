package com.woowa.weatherfit.domain.repository

import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.SubCategory
import java.io.File

interface ClothRepository {
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
