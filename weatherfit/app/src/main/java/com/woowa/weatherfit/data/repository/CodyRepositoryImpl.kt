package com.woowa.weatherfit.data.repository

import com.woowa.weatherfit.data.local.dao.ClothDao
import com.woowa.weatherfit.data.local.dao.CodyDao
import com.woowa.weatherfit.data.local.entity.CodyEntity
import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.repository.CodyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CodyRepositoryImpl @Inject constructor(
    private val codyDao: CodyDao,
    private val clothDao: ClothDao
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
                val clothes = clothDao.getClothesByIds(cody.clothIds).map { it.toDomain() }
                CodyWithClothes(cody, clothes)
            }
        }
    }

    override fun getAllCodiesWithClothes(): Flow<List<CodyWithClothes>> {
        return codyDao.getAllCodies().map { codyEntities ->
            codyEntities.map { entity ->
                val cody = entity.toDomain()
                val clothes = clothDao.getClothesByIds(cody.clothIds).map { it.toDomain() }
                CodyWithClothes(cody, clothes)
            }
        }
    }

    override fun getCodiesWithClothesBySeason(season: Season): Flow<List<CodyWithClothes>> {
        return codyDao.getCodiesBySeason(season.name).map { codyEntities ->
            codyEntities.map { entity ->
                val cody = entity.toDomain()
                val clothes = clothDao.getClothesByIds(cody.clothIds).map { it.toDomain() }
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
}
