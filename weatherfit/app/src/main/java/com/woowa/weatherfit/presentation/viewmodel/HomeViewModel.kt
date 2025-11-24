package com.woowa.weatherfit.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowa.weatherfit.domain.model.Region
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.model.TemperatureRange
import com.woowa.weatherfit.domain.repository.RegionRepository
import com.woowa.weatherfit.domain.usecase.location.GetCurrentLocationUseCase
import com.woowa.weatherfit.domain.usecase.region.GetSelectedRegionUseCase
import com.woowa.weatherfit.domain.usecase.today.GetTodayRecommendationUseCase
import com.woowa.weatherfit.presentation.state.HomeUiState
import com.woowa.weatherfit.presentation.state.OutfitRecommendation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayRecommendationUseCase: GetTodayRecommendationUseCase,
    private val getSelectedRegionUseCase: GetSelectedRegionUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val regionRepository: RegionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentRegion: Region? = null

    init {
        observeRegion()
    }

    fun updateLocationToCurrentPosition() {
        Log.d(TAG, "updateLocationToCurrentPosition() called")
        viewModelScope.launch {
            getCurrentLocationUseCase()
                .onSuccess { locationData ->
                    Log.d(TAG, "Location received from UseCase - Lat: ${locationData.latitude}, Lon: ${locationData.longitude}")

                    // 디버그 정보 업데이트
                    val debugInfo = "GPS: ${String.format("%.4f", locationData.latitude)}, ${String.format("%.4f", locationData.longitude)}"
                    _uiState.update { it.copy(debugGpsInfo = debugInfo) }

                    val nearestRegion = regionRepository.findNearestRegion(
                        locationData.latitude,
                        locationData.longitude
                    )

                    Log.d(TAG, "Nearest region found: ${nearestRegion.name} (${nearestRegion.province.displayName})")
                    Log.d(TAG, "Region coordinates - Lat: ${nearestRegion.latitude}, Lon: ${nearestRegion.longitude}")

                    regionRepository.setSelectedRegion(nearestRegion)
                    Log.d(TAG, "Selected region updated to: ${nearestRegion.name}")
                }
                .onFailure { error ->
                    Log.e(TAG, "Failed to get current location: ${error.message}", error)
                    _uiState.update { it.copy(debugGpsInfo = "GPS 실패: ${error.message}") }
                }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private fun observeRegion() {
        viewModelScope.launch {
            getSelectedRegionUseCase().collectLatest { region ->
                currentRegion = region
                loadTodayData(region)
            }
        }
    }

    private fun loadTodayData(region: Region) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getTodayRecommendationUseCase(
                latitude = region.latitude,
                longitude = region.longitude,
                locationName = region.name
            ).onSuccess { todayResponse ->
                // 온도로부터 계절 계산
                val temperatureRange = TemperatureRange.fromTemperature(todayResponse.temperature.toInt())
                val season = Season.fromTemperatureRange(temperatureRange)

                // Outfit 리스트 변환
                val outfits = todayResponse.outfits.map { outfit ->
                    OutfitRecommendation(
                        id = outfit.id,
                        thumbnail = outfit.thumbnail
                    )
                }

                _uiState.update {
                    it.copy(
                        regionName = todayResponse.region,
                        temperature = todayResponse.temperature,
                        weatherCondition = todayResponse.weather,
                        currentSeason = season,
                        recommendedOutfits = outfits,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }
        }
    }

    fun refresh() {
        currentRegion?.let { loadTodayData(it) }
    }
}
