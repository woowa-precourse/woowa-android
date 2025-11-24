package com.woowa.weatherfit.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.woowa.weatherfit.domain.model.Region
import com.woowa.weatherfit.domain.model.Regions
import com.woowa.weatherfit.domain.repository.RegionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "region_prefs")

class RegionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : RegionRepository {

    private object PreferencesKeys {
        val REGION_ID = longPreferencesKey("region_id")
        val REGION_NAME = stringPreferencesKey("region_name")
        val REGION_LAT = doublePreferencesKey("region_lat")
        val REGION_LON = doublePreferencesKey("region_lon")
    }

    override fun getSelectedRegion(): Flow<Region> {
        return context.dataStore.data.map { preferences ->
            val regionId = preferences[PreferencesKeys.REGION_ID] ?: Regions.DEFAULT.id
            // ID로 Regions.LIST에서 찾아서 반환 (province 정보 포함)
            Regions.LIST.find { it.id == regionId } ?: Regions.DEFAULT
        }
    }

    override suspend fun setSelectedRegion(region: Region) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REGION_ID] = region.id
            preferences[PreferencesKeys.REGION_NAME] = region.name
            preferences[PreferencesKeys.REGION_LAT] = region.latitude
            preferences[PreferencesKeys.REGION_LON] = region.longitude
        }
    }

    override fun searchRegions(query: String): List<Region> {
        return if (query.isBlank()) {
            Regions.LIST
        } else {
            Regions.LIST.filter { it.name.contains(query, ignoreCase = true) }
        }
    }

    override fun getAllRegions(): List<Region> = Regions.LIST

    override fun findNearestRegion(latitude: Double, longitude: Double): Region {
        Log.d(TAG, "========================================")
        Log.d(TAG, "Finding nearest region for GPS coordinates:")
        Log.d(TAG, "Current Location - Lat: $latitude, Lon: $longitude")
        Log.d(TAG, "========================================")

        val distanceMap = mutableMapOf<Region, Double>()

        Regions.LIST.forEach { region ->
            val distance = calculateDistance(latitude, longitude, region.latitude, region.longitude)
            distanceMap[region] = distance
        }

        // 거리순으로 정렬해서 가장 가까운 10개 출력
        val sorted = distanceMap.entries.sortedBy { it.value }.take(10)
        Log.d(TAG, "Top 10 closest regions:")
        sorted.forEachIndexed { index, entry ->
            Log.d(TAG, "${index + 1}. ${entry.key.name} (${entry.key.province.displayName}): ${String.format("%.2f", entry.value)}km")
        }

        val nearestRegion = sorted.firstOrNull()?.key ?: Regions.DEFAULT
        Log.d(TAG, "========================================")
        Log.d(TAG, "Selected nearest region: ${nearestRegion.name}")
        Log.d(TAG, "Distance: ${String.format("%.2f", distanceMap[nearestRegion] ?: 0.0)}km")
        Log.d(TAG, "========================================")

        return nearestRegion
    }

    companion object {
        private const val TAG = "RegionRepository"
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0 // 지구 반지름 (km)

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }
}
