package com.woowa.weatherfit.domain.repository

import com.woowa.weatherfit.domain.model.Region
import kotlinx.coroutines.flow.Flow

interface RegionRepository {
    fun getSelectedRegion(): Flow<Region>

    suspend fun setSelectedRegion(region: Region)

    fun searchRegions(query: String): List<Region>

    fun getAllRegions(): List<Region>

    fun findNearestRegion(latitude: Double, longitude: Double): Region
}
