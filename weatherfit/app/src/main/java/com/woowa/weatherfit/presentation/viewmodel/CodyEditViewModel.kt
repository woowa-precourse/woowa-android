package com.woowa.weatherfit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.CodyClothItem
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
import java.io.File
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
            val currentPositions = state.clothItemsWithPosition.toMutableMap()

            if (currentSelected.any { it.id == cloth.id }) {
                currentSelected.removeAll { it.id == cloth.id }
                currentPositions.remove(cloth.id)
            } else {
                currentSelected.add(cloth)
                val zIndex = currentPositions.size
                currentPositions[cloth.id] = CodyClothItem(
                    id = cloth.id,
                    xCoord = 0.5,
                    yCoord = 0.5,
                    zIndex = zIndex,
                    scale = 1.0
                )
            }
            state.copy(
                selectedClothes = currentSelected,
                clothItemsWithPosition = currentPositions
            )
        }
    }

    fun removeSelectedCloth(cloth: Cloth) {
        _uiState.update { state ->
            val updatedPositions = state.clothItemsWithPosition.toMutableMap()
            updatedPositions.remove(cloth.id)
            state.copy(
                selectedClothes = state.selectedClothes.filter { it.id != cloth.id },
                clothItemsWithPosition = updatedPositions
            )
        }
    }

    fun updateClothPosition(clothId: Long, xCoord: Double, yCoord: Double, scale: Double = 1.0) {
        _uiState.update { state ->
            val currentPosition = state.clothItemsWithPosition[clothId]
            if (currentPosition != null) {
                val updatedPositions = state.clothItemsWithPosition.toMutableMap()
                updatedPositions[clothId] = currentPosition.copy(
                    xCoord = xCoord,
                    yCoord = yCoord,
                    scale = scale
                )
                state.copy(clothItemsWithPosition = updatedPositions)
            } else {
                state
            }
        }
    }

    fun updateClothZIndex(clothId: Long, zIndex: Int) {
        _uiState.update { state ->
            val currentPosition = state.clothItemsWithPosition[clothId]
            if (currentPosition != null) {
                val updatedPositions = state.clothItemsWithPosition.toMutableMap()
                updatedPositions[clothId] = currentPosition.copy(zIndex = zIndex)
                state.copy(clothItemsWithPosition = updatedPositions)
            } else {
                state
            }
        }
    }

    fun setThumbnail(file: File) {
        _uiState.update { it.copy(thumbnailFile = file) }
    }

    fun saveCody() {
        val state = _uiState.value

        if (state.selectedClothes.isEmpty()) {
            _uiState.update { it.copy(error = "옷을 선택해주세요") }
            return
        }

        if (state.thumbnailFile == null) {
            _uiState.update { it.copy(error = "썸네일을 설정해주세요") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                val clothItems = state.clothItemsWithPosition.values.toList()

                val result = addCodyUseCase(
                    thumbnail = state.thumbnailFile,
                    clothItems = clothItems,
                    category = state.selectedSeason
                )

                result.onSuccess {
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                }.onFailure { e ->
                    _uiState.update {
                        it.copy(isSaving = false, error = e.message ?: "저장 중 오류가 발생했습니다")
                    }
                }
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
