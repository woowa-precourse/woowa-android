package com.woowa.weatherfit.data.repository

import com.woowa.weatherfit.data.remote.api.ClothesApi
import com.woowa.weatherfit.data.remote.dto.ClothesRegisterRequest
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.repository.ClothRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ClothRepositoryImpl @Inject constructor(
    private val clothesApi: ClothesApi,
    private val json: Json
) : ClothRepository {

    // Server API methods
    override suspend fun registerClothesToServer(
        imageFile: File,
        category: MainCategory,
        subCategory: SubCategory
    ): Result<Cloth> = runCatching {
        val imagePart = MultipartBody.Part.createFormData(
            name = "image",
            filename = imageFile.name,
            body = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        )

        val response = clothesApi.registerClothes(
            image = imagePart,
            category = category.name.uppercase(),
            subCategory = subCategory.serverValue.uppercase()
        )
        response.toDomain()
    }

    override suspend fun updateClothesOnServer(
        clothesId: Long,
        category: MainCategory,
        subCategory: SubCategory
    ): Result<Cloth> = runCatching {
        val response = clothesApi.updateClothes(
            clothesId = clothesId,
            category = category.name.uppercase(),
            subCategory = subCategory.serverValue.uppercase()
        )
        response.toDomain()
    }

    override suspend fun deleteClothesFromServer(clothesId: Long): Result<Unit> = runCatching {
        clothesApi.deleteClothes(clothesId)
    }

    override suspend fun fetchClothesDetailFromServer(clothesId: Long): Result<Cloth> = runCatching {
        val response = clothesApi.getClothesDetail(clothesId)
        response.toDomain()
    }

    override suspend fun fetchClothesListFromServer(
        category: MainCategory?,
        sub: SubCategory?,
        cursor: Long?,
        size: Int
    ): Result<List<Cloth>> = runCatching {
        val response = clothesApi.getClothesList(
            category = category?.name?.uppercase(),
            sub = sub?.serverValue?.uppercase(),
            cursor = cursor,
            size = size
        )

        response.map { it.toDomain() }
    }
}
