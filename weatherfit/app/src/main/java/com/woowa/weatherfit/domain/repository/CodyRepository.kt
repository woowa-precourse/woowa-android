package com.woowa.weatherfit.domain.repository

import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyClothItem
import com.woowa.weatherfit.domain.model.Season
import java.io.File

interface CodyRepository {
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

    suspend fun toggleFixed(id: Long)
}
