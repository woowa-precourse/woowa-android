package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.repository.CodyRepository
import javax.inject.Inject

class ToggleCodyFixedUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    suspend operator fun invoke(id: Long): Result<Boolean> {
        return try {
            val isFixed = codyRepository.toggleFixed(id)
            Result.success(isFixed)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
