package com.woowa.weatherfit.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.usecase.cloth.GetAllClothesUseCase
import com.woowa.weatherfit.domain.usecase.cody.DeleteCodyUseCase
import com.woowa.weatherfit.domain.usecase.cody.GetAllCodiesUseCase
import com.woowa.weatherfit.domain.usecase.cody.GetCodyDetailUseCase
import com.woowa.weatherfit.domain.usecase.cody.ToggleCodyFixedUseCase
import com.woowa.weatherfit.presentation.state.CodyDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CodyDetailViewModel @Inject constructor(
    private val getCodyDetailUseCase: GetCodyDetailUseCase,
    private val getAllClothesUseCase: GetAllClothesUseCase,
    private val getAllCodiesUseCase: GetAllCodiesUseCase,
    private val toggleCodyFixedUseCase: ToggleCodyFixedUseCase,
    private val deleteCodyUseCase: DeleteCodyUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val codyId: Long = savedStateHandle.get<Long>("codyId") ?: 0L
    val currentCodyId: Long = codyId

    private val _uiState = MutableStateFlow(CodyDetailUiState())
    val uiState: StateFlow<CodyDetailUiState> = _uiState.asStateFlow()

    init {
        loadCodyDetail()
    }

    fun refreshCodyDetail() {
        loadCodyDetail()
    }

    private fun loadCodyDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Get cody detail, all clothes, and all codies (to check fixed status)
                val codyResult = getCodyDetailUseCase(codyId)
                val clothesResult = getAllClothesUseCase()
                val allCodiesResult = getAllCodiesUseCase()

                if (codyResult.isSuccess && clothesResult.isSuccess && allCodiesResult.isSuccess) {
                    val cody = codyResult.getOrNull()!!
                    val allClothes = clothesResult.getOrNull()!!
                    val (fixedCodies, regularCodies) = allCodiesResult.getOrNull()!!

                    // Check if this cody is in the fixed list
                    val isFixed = fixedCodies.any { it.id == codyId }

                    // Update cody with correct fixed status
                    val updatedCody = cody.copy(isFixed = isFixed)

                    // Match clothItems with actual Cloth objects
                    val clothes = updatedCody.clothItems.mapNotNull { item ->
                        allClothes.find { it.id == item.id }
                    }

                    val codyWithClothes = CodyWithClothes(updatedCody, clothes)
                    _uiState.update {
                        it.copy(
                            codyWithClothes = codyWithClothes,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    val errorMsg = codyResult.exceptionOrNull()?.message
                        ?: clothesResult.exceptionOrNull()?.message
                        ?: allCodiesResult.exceptionOrNull()?.message
                        ?: "데이터를 불러올 수 없습니다"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "로딩 실패: $errorMsg"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "오류 발생: ${e.message}"
                    )
                }
            }
        }
    }

    fun selectClothIndex(index: Int) {
        _uiState.update { it.copy(selectedClothIndex = index) }
    }

    fun toggleFixed() {
        viewModelScope.launch {
            toggleCodyFixedUseCase(codyId).onSuccess { isFixed ->
                // Update the UI state with the new fixed status
                _uiState.update { currentState ->
                    currentState.codyWithClothes?.let { codyWithClothes ->
                        val updatedCody = codyWithClothes.cody.copy(isFixed = isFixed)
                        val updatedCodyWithClothes = codyWithClothes.copy(cody = updatedCody)
                        currentState.copy(codyWithClothes = updatedCodyWithClothes)
                    } ?: currentState
                }
            }
        }
    }

    fun deleteCody(onDeleted: () -> Unit) {
        viewModelScope.launch {
            deleteCodyUseCase(codyId).onSuccess {
                onDeleted()
            }
        }
    }
}
