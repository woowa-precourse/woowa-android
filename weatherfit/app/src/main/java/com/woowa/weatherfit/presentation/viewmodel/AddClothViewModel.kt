package com.woowa.weatherfit.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.ClothColor
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.model.TemperatureRange
import com.woowa.weatherfit.domain.usecase.cloth.AddClothUseCase
import com.woowa.weatherfit.domain.usecase.cloth.GetClothByIdUseCase
import com.woowa.weatherfit.domain.usecase.cloth.UpdateClothUseCase
import com.woowa.weatherfit.presentation.state.AddClothUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddClothViewModel @Inject constructor(
    private val addClothUseCase: AddClothUseCase,
    private val updateClothUseCase: UpdateClothUseCase,
    private val getClothByIdUseCase: GetClothByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddClothUiState())
    val uiState: StateFlow<AddClothUiState> = _uiState.asStateFlow()

    fun loadCloth(clothId: Long) {
        viewModelScope.launch {
            getClothByIdUseCase(clothId).onSuccess { cloth ->
                _uiState.update {
                    it.copy(
                        clothId = clothId,
                        imageUri = Uri.parse(cloth.imageUrl),
                        selectedMainCategory = cloth.mainCategory,
                        selectedSubCategory = cloth.subCategory,
                        selectedTemperatureRange = cloth.temperatureRange,
                        selectedColor = cloth.color,
                        availableSubCategories = SubCategory.getByMainCategory(cloth.mainCategory),
                        isEditMode = true
                    )
                }
            }
        }
    }

    fun setImageUri(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun selectMainCategory(mainCategory: MainCategory) {
        val subCategories = SubCategory.getByMainCategory(mainCategory)
        _uiState.update { state ->
            state.copy(
                selectedMainCategory = mainCategory,
                availableSubCategories = subCategories,
                selectedSubCategory = subCategories.firstOrNull() ?: state.selectedSubCategory
            )
        }
    }

    fun selectSubCategory(subCategory: SubCategory) {
        _uiState.update { it.copy(selectedSubCategory = subCategory) }
    }

    fun selectTemperatureRange(temperatureRange: TemperatureRange) {
        _uiState.update { it.copy(selectedTemperatureRange = temperatureRange) }
    }

    fun selectColor(color: ClothColor?) {
        _uiState.update { it.copy(selectedColor = color) }
    }

    fun saveCloth() {
        val state = _uiState.value

        if (state.imageUri == null) {
            _uiState.update { it.copy(error = "이미지를 선택해주세요") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            val cloth = Cloth(
                id = state.clothId ?: 0L,
                imageUrl = "",
                mainCategory = state.selectedMainCategory,
                subCategory = state.selectedSubCategory,
                temperatureRange = state.selectedTemperatureRange,
                color = state.selectedColor
            )

            val result = if (state.isEditMode && state.clothId != null) {
                updateClothUseCase(cloth)
            } else {
                addClothUseCase(state.imageUri!!, cloth)
            }

            result.onSuccess {
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(isSaving = false, error = e.message ?: "저장 중 오류가 발생했습니다")
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = AddClothUiState()
    }
}
