package com.woowa.weatherfit.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.usecase.cody.DeleteCodyUseCase
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

    private fun loadCodyDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getCodyDetailUseCase(codyId).collectLatest { codyWithClothes ->
                _uiState.update {
                    it.copy(
                        codyWithClothes = codyWithClothes,
                        isLoading = false
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
            toggleCodyFixedUseCase(codyId)
        }
    }

    fun deleteCody() {
        viewModelScope.launch {
            deleteCodyUseCase(codyId)
        }
    }
}
