package com.woowa.weatherfit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.usecase.cloth.GetAllClothesUseCase
import com.woowa.weatherfit.domain.usecase.cody.AddCodyUseCase
import com.woowa.weatherfit.presentation.state.CodyEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CodyEditViewModel @Inject constructor(
    private val getAllClothesUseCase: GetAllClothesUseCase,
    private val addCodyUseCase: AddCodyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CodyEditUiState())
    val uiState: StateFlow<CodyEditUiState> = _uiState.asStateFlow()

    init {
        loadClothes()
    }

    private fun loadClothes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getAllClothesUseCase().collectLatest { clothes ->
                _uiState.update { state ->
                    state.copy(
                        allClothes = clothes,
                        isLoading = false
                    )
                }
                applyFilters()
            }
        }
    }

    fun selectSeason(season: Season) {
        _uiState.update { it.copy(selectedSeason = season) }
    }

    fun selectMainCategory(mainCategory: MainCategory) {
        val subCategories = SubCategory.getByMainCategory(mainCategory)
        _uiState.update { state ->
            state.copy(
                selectedMainCategory = mainCategory,
                selectedSubCategory = null,
                availableSubCategories = subCategories
            )
        }
        applyFilters()
    }

    fun selectSubCategory(subCategory: SubCategory?) {
        _uiState.update { it.copy(selectedSubCategory = subCategory) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val filtered = state.allClothes.filter { cloth ->
            cloth.mainCategory == state.selectedMainCategory &&
                (state.selectedSubCategory == null || cloth.subCategory == state.selectedSubCategory)
        }
        _uiState.update { it.copy(filteredClothes = filtered) }
    }

    fun toggleClothSelection(cloth: Cloth) {
        _uiState.update { state ->
            val currentSelected = state.selectedClothes.toMutableList()
            if (currentSelected.any { it.id == cloth.id }) {
                currentSelected.removeAll { it.id == cloth.id }
            } else {
                currentSelected.add(cloth)
            }
            state.copy(selectedClothes = currentSelected)
        }
    }

    fun removeSelectedCloth(cloth: Cloth) {
        _uiState.update { state ->
            state.copy(selectedClothes = state.selectedClothes.filter { it.id != cloth.id })
        }
    }

    fun saveCody() {
        val state = _uiState.value

        if (state.selectedClothes.isEmpty()) {
            _uiState.update { it.copy(error = "옷을 선택해주세요") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                val cody = Cody(
                    clothIds = state.selectedClothes.map { it.id },
                    season = state.selectedSeason
                )
                addCodyUseCase(cody)
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSaving = false, error = e.message ?: "저장 중 오류가 발생했습니다")
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = CodyEditUiState()
    }
}
