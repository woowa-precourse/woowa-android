package com.woowa.weatherfit.data.repository

import com.woowa.weatherfit.data.local.dao.ClothDao
import com.woowa.weatherfit.data.local.dao.CodyDao
import com.woowa.weatherfit.data.local.entity.CodyEntity
import com.woowa.weatherfit.data.remote.api.OutfitApi
import com.woowa.weatherfit.data.remote.dto.CreateOutfitRequest
import com.woowa.weatherfit.data.remote.dto.toCody
import com.woowa.weatherfit.data.remote.dto.toClothesRequest
import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyClothItem
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.repository.CodyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class CodyRepositoryImpl @Inject constructor(
    private val codyDao: CodyDao,
    private val clothDao: ClothDao,
    private val outfitApi: OutfitApi
) : CodyRepository {

    override fun getAllCodies(): Flow<List<Cody>> {
        return codyDao.getAllCodies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCodiesBySeason(season: Season): Flow<List<Cody>> {
        return codyDao.getCodiesBySeason(season.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCodyWithClothes(codyId: Long): Flow<CodyWithClothes?> {
        return codyDao.getCodyByIdFlow(codyId).map { codyEntity ->
            codyEntity?.let { entity ->
                val cody = entity.toDomain()
                val clothIds = cody.clothItems.map { it.id }
                val clothes = clothDao.getClothesByIds(clothIds).map { it.toDomain() }
                CodyWithClothes(cody, clothes)
            }
        }
    }

    override fun getAllCodiesWithClothes(): Flow<List<CodyWithClothes>> {
        return codyDao.getAllCodies().map { codyEntities ->
            codyEntities.map { entity ->
                val cody = entity.toDomain()
                val clothIds = cody.clothItems.map { it.id }
                val clothes = clothDao.getClothesByIds(clothIds).map { it.toDomain() }
                CodyWithClothes(cody, clothes)
            }
        }
    }

    override fun getCodiesWithClothesBySeason(season: Season): Flow<List<CodyWithClothes>> {
        return codyDao.getCodiesBySeason(season.name).map { codyEntities ->
            codyEntities.map { entity ->
                val cody = entity.toDomain()
                val clothIds = cody.clothItems.map { it.id }
                val clothes = clothDao.getClothesByIds(clothIds).map { it.toDomain() }
                CodyWithClothes(cody, clothes)
            }
        }
    }

    override suspend fun getCodyById(id: Long): Cody? {
        return codyDao.getCodyById(id)?.toDomain()
    }

    override suspend fun insertCody(cody: Cody): Long {
        return codyDao.insertCody(CodyEntity.fromDomain(cody))
    }

    override suspend fun updateCody(cody: Cody) {
        codyDao.updateCody(CodyEntity.fromDomain(cody))
    }

    override suspend fun deleteCody(cody: Cody) {
        codyDao.deleteCody(CodyEntity.fromDomain(cody))
    }

    override suspend fun deleteCodyById(id: Long) {
        codyDao.deleteCodyById(id)
    }

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

    override suspend fun syncWithRemote() {
        try {
            val (fixedOutfits, regularOutfits) = getOutfitsRemote()
            val allRemoteOutfits = fixedOutfits + regularOutfits

            codyDao.deleteAll()

            allRemoteOutfits.forEach { cody ->
                codyDao.insertCody(CodyEntity.fromDomain(cody))
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun toggleFixed(id: Long) {
        val cody = getCodyById(id) ?: return
        val updatedCody = cody.copy(isFixed = !cody.isFixed)
        updateCody(updatedCody)
    }
}
