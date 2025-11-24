package com.woowa.weatherfit.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.deviceIdDataStore: DataStore<Preferences> by preferencesDataStore(name = "device_id_prefs")

@Singleton
class DeviceIdDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val DEVICE_ID = stringPreferencesKey("device_id")
    }

    fun getDeviceId(): Flow<String> {
        return context.deviceIdDataStore.data.map { preferences ->
            preferences[Keys.DEVICE_ID] ?: ""
        }
    }

    suspend fun getOrCreateDeviceId(): String {

        val currentId = getDeviceId().first()
        return if (currentId.isNotBlank()) {
            currentId
        } else {
            val newId = UUID.randomUUID().toString()
            context.deviceIdDataStore.edit { preferences ->
                preferences[Keys.DEVICE_ID] = newId
            }
            newId
        }

    }
}
