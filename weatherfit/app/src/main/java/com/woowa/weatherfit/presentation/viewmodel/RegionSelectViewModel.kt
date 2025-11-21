package com.woowa.weatherfit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowa.weatherfit.domain.model.Region
import com.woowa.weatherfit.domain.usecase.region.SearchRegionsUseCase
import com.woowa.weatherfit.domain.usecase.region.SetSelectedRegionUseCase
import com.woowa.weatherfit.presentation.state.RegionSelectUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegionSelectViewModel @Inject constructor(
    private val searchRegionsUseCase: SearchRegionsUseCase,
    private val setSelectedRegionUseCase: SetSelectedRegionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegionSelectUiState())
    val uiState: StateFlow<RegionSelectUiState> = _uiState.asStateFlow()

    init {
        loadRegions()
    }

    private fun loadRegions() {
        val regions = searchRegionsUseCase("")
        _uiState.update { it.copy(regions = regions) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        val regions = searchRegionsUseCase(query)
        _uiState.update { it.copy(regions = regions) }
    }

    fun selectRegion(region: Region) {
        _uiState.update { it.copy(selectedRegion = region) }
    }

    fun saveSelectedRegion() {
        val region = _uiState.value.selectedRegion ?: return

        viewModelScope.launch {
            setSelectedRegionUseCase(region)
            _uiState.update { it.copy(saveSuccess = true) }
        }
    }
}
