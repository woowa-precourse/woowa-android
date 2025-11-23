package com.woowa.weatherfit.data.repository

import com.woowa.weatherfit.data.remote.api.OutfitApi
import com.woowa.weatherfit.data.remote.dto.toCody
import com.woowa.weatherfit.data.remote.dto.toClothesRequest
import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyClothItem
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.repository.CodyRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class CodyRepositoryImpl @Inject constructor(
    private val outfitApi: OutfitApi
) : CodyRepository {

    // Remote API operations
    override suspend fun createOutfitRemote(
        thumbnail: File,
        clothItems: List<CodyClothItem>,
        category: Season
    ): Cody {
        val thumbnailPart = MultipartBody.Part.createFormData(
            "thumbnail",
            thumbnail.name,
            thumbnail.asRequestBody("image/*".toMediaTypeOrNull())
        )

        val clothesJson = Json.encodeToString(clothItems.map { it.toClothesRequest() })
        val clothesPart = clothesJson.toRequestBody("application/json".toMediaTypeOrNull())

        val categoryPart = category.name.toRequestBody("text/plain".toMediaTypeOrNull())

        val response = outfitApi.createOutfit(thumbnailPart, clothesPart, categoryPart)
        return response.toCody()
    }

    override suspend fun getOutfitsRemote(): Pair<List<Cody>, List<Cody>> {
        val response = outfitApi.getOutfits()
        val fixedCodies = response.fixedOutfits.map { it.toCody(isFixed = true) }
        val regularCodies = response.outfits.map { it.toCody(isFixed = false) }
        return Pair(fixedCodies, regularCodies)
    }

    override suspend fun getOutfitDetailRemote(id: Long): Cody {
        val response = outfitApi.getOutfitDetail(id)
        return Cody(
            id = response.id,
            thumbnail = response.thumbnail,
            clothItems = emptyList(),
            category = Season.valueOf(response.category.uppercase()),
            isFixed = false
        )
    }

    override suspend fun updateOutfitRemote(
        id: Long,
        clothItems: List<CodyClothItem>,
        category: Season
    ): Cody {
        val clothesJson = Json.encodeToString(clothItems.map { it.toClothesRequest() })
        val clothesPart = clothesJson.toRequestBody("application/json".toMediaTypeOrNull())

        val categoryPart = category.name.toRequestBody("text/plain".toMediaTypeOrNull())

        val response = outfitApi.updateOutfit(id, clothesPart, categoryPart)
        return response.toCody()
    }

    override suspend fun deleteOutfitRemote(id: Long) {
        outfitApi.deleteOutfit(id)
    }

    override suspend fun toggleFixed(id: Long) {
        // TODO: 서버 API에 toggleFixed 엔드포인트 추가 필요
    }
}
