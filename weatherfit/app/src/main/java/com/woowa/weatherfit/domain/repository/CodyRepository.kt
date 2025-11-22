package com.woowa.weatherfit.domain.repository

import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyClothItem
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.model.Season
import kotlinx.coroutines.flow.Flow
import java.io.File

interface CodyRepository {
    // Local operations
    fun getAllCodies(): Flow<List<Cody>>

    fun getCodiesBySeason(season: Season): Flow<List<Cody>>

    fun getCodyWithClothes(codyId: Long): Flow<CodyWithClothes?>

    fun getAllCodiesWithClothes(): Flow<List<CodyWithClothes>>

    fun getCodiesWithClothesBySeason(season: Season): Flow<List<CodyWithClothes>>

    suspend fun getCodyById(id: Long): Cody?

    suspend fun insertCody(cody: Cody): Long

    suspend fun updateCody(cody: Cody)

    suspend fun deleteCody(cody: Cody)

    suspend fun deleteCodyById(id: Long)

    // Remote operations
    suspend fun createOutfitRemote(
        thumbnail: File,
        clothItems: List<CodyClothItem>,
        category: Season
    ): Cody

    suspend fun getOutfitsRemote(): Pair<List<Cody>, List<Cody>> // Fixed, Regular

    suspend fun getOutfitDetailRemote(id: Long): Cody

    suspend fun updateOutfitRemote(
        id: Long,
        clothItems: List<CodyClothItem>,
        category: Season
    ): Cody

    suspend fun deleteOutfitRemote(id: Long)

    // Synchronization
    suspend fun syncWithRemote()

    suspend fun toggleFixed(id: Long)
}
