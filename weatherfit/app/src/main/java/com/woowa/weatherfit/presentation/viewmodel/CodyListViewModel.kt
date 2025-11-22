package com.woowa.weatherfit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.usecase.cody.DeleteCodyUseCase
import com.woowa.weatherfit.domain.usecase.cody.GetAllCodiesUseCase
import com.woowa.weatherfit.presentation.state.CodyListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CodyListViewModel @Inject constructor(
    private val getAllCodiesUseCase: GetAllCodiesUseCase,
    private val deleteCodyUseCase: DeleteCodyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CodyListUiState())
    val uiState: StateFlow<CodyListUiState> = _uiState.asStateFlow()

    init {
        loadCodies()
    }

    private fun loadCodies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getAllCodiesUseCase().collectLatest { codies ->
                val fixedCodies = codies.filter { it.cody.isFixed }
                val regularCodies = codies.filter { !it.cody.isFixed }
                _uiState.update {
                    it.copy(
                        codies = codies,
                        fixedCodies = fixedCodies,
                        regularCodies = regularCodies,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun toggleEditMode() {
        _uiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun deleteCody(codyId: Long) {
        viewModelScope.launch {
            deleteCodyUseCase(codyId)
        }
    }
}
