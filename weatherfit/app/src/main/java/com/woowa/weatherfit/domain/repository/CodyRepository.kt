package com.woowa.weatherfit.domain.repository

import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.model.Season
import kotlinx.coroutines.flow.Flow

interface CodyRepository {
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
}
