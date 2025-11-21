package com.woowa.weatherfit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.model.Region
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.model.TemperatureRange
import com.woowa.weatherfit.domain.model.Weather
import com.woowa.weatherfit.domain.usecase.cody.GetCodiesBySeasonUseCase
import com.woowa.weatherfit.domain.usecase.region.GetSelectedRegionUseCase
import com.woowa.weatherfit.domain.usecase.weather.GetWeatherUseCase
import com.woowa.weatherfit.presentation.state.HomeUiState
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
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getSelectedRegionUseCase: GetSelectedRegionUseCase,
    private val getCodiesBySeasonUseCase: GetCodiesBySeasonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeRegion()
    }

    private fun observeRegion() {
        viewModelScope.launch {
            getSelectedRegionUseCase().collectLatest { region ->
                _uiState.update { it.copy(region = region) }
                loadWeather(region)
            }
        }
    }

    private fun loadWeather(region: Region) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getWeatherUseCase(region.latitude, region.longitude)
                .onSuccess { weather ->
                    val temperatureRange = TemperatureRange.fromTemperature(weather.temperature)
                    val season = Season.fromTemperatureRange(temperatureRange)

                    _uiState.update {
                        it.copy(
                            weather = weather,
                            currentSeason = season,
                            isLoading = false
                        )
                    }
                    loadRecommendedCodies(season)
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    private fun loadRecommendedCodies(season: Season) {
        viewModelScope.launch {
            getCodiesBySeasonUseCase(season).collectLatest { codies ->
                _uiState.update { it.copy(recommendedCodies = codies) }
            }
        }
    }

    fun refresh() {
        _uiState.value.region?.let { loadWeather(it) }
    }
}
