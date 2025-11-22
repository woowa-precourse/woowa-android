package com.woowa.weatherfit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.usecase.cloth.DeleteClothUseCase
import com.woowa.weatherfit.domain.usecase.cloth.GetAllClothesUseCase
import com.woowa.weatherfit.domain.usecase.cloth.GetClothesByCategoryUseCase
import com.woowa.weatherfit.presentation.state.ClothListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClothListViewModel @Inject constructor(
    private val getAllClothesUseCase: GetAllClothesUseCase,
    private val getClothesByCategoryUseCase: GetClothesByCategoryUseCase,
    private val deleteClothUseCase: DeleteClothUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClothListUiState())
    val uiState: StateFlow<ClothListUiState> = _uiState.asStateFlow()

    init {
        loadClothes()
    }

    private fun loadClothes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getAllClothesUseCase().collectLatest { clothes ->
                _uiState.update { state ->
                    state.copy(
                        clothes = clothes,
                        isLoading = false
                    )
                }
                applyFilters()
            }
        }
    }

    fun selectMainCategory(mainCategory: MainCategory) {
        _uiState.update { state ->
            state.copy(
                selectedMainCategory = mainCategory,
                selectedSubCategory = null,
                availableSubCategories = SubCategory.getByMainCategory(mainCategory)
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
        val filtered = state.clothes.filter { cloth ->
            cloth.mainCategory == state.selectedMainCategory &&
                (state.selectedSubCategory == null || cloth.subCategory == state.selectedSubCategory)
        }
        _uiState.update { it.copy(filteredClothes = filtered) }
    }

    fun deleteCloth(clothId: Long) {
        viewModelScope.launch {
            deleteClothUseCase(clothId).onFailure { e ->
                // TODO: 에러 처리 (Toast 또는 Snackbar 표시)
            }
        }
    }
}
