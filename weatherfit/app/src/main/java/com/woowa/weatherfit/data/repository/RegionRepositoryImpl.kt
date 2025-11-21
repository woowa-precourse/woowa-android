package com.woowa.weatherfit.data.repository

import android.content.Context
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
            Region(
                id = preferences[PreferencesKeys.REGION_ID] ?: Regions.DEFAULT.id,
                name = preferences[PreferencesKeys.REGION_NAME] ?: Regions.DEFAULT.name,
                latitude = preferences[PreferencesKeys.REGION_LAT] ?: Regions.DEFAULT.latitude,
                longitude = preferences[PreferencesKeys.REGION_LON] ?: Regions.DEFAULT.longitude
            )
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
}
