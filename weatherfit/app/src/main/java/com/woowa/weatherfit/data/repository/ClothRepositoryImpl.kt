package com.woowa.weatherfit.data.repository

import com.woowa.weatherfit.data.local.dao.ClothDao
import com.woowa.weatherfit.data.local.entity.ClothEntity
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.model.TemperatureRange
import com.woowa.weatherfit.domain.repository.ClothRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClothRepositoryImpl @Inject constructor(
    private val clothDao: ClothDao
) : ClothRepository {

    override fun getAllClothes(): Flow<List<Cloth>> {
        return clothDao.getAllClothes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getClothesByMainCategory(mainCategory: MainCategory): Flow<List<Cloth>> {
        return clothDao.getClothesByMainCategory(mainCategory.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getClothesBySubCategory(subCategory: SubCategory): Flow<List<Cloth>> {
        return clothDao.getClothesBySubCategory(subCategory.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getClothesByTemperatureRange(temperatureRange: TemperatureRange): Flow<List<Cloth>> {
        return clothDao.getClothesByTemperatureRange(temperatureRange.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getClothById(id: Long): Cloth? {
        return clothDao.getClothById(id)?.toDomain()
    }

    override suspend fun insertCloth(cloth: Cloth): Long {
        return clothDao.insertCloth(ClothEntity.fromDomain(cloth))
    }

    override suspend fun updateCloth(cloth: Cloth) {
        clothDao.updateCloth(ClothEntity.fromDomain(cloth))
    }

    override suspend fun deleteCloth(cloth: Cloth) {
        clothDao.deleteCloth(ClothEntity.fromDomain(cloth))
    }

    override suspend fun deleteClothById(id: Long) {
        clothDao.deleteClothById(id)
    }
}
