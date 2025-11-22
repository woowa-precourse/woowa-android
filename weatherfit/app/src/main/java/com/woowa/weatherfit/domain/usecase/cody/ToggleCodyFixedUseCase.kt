package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.repository.CodyRepository
import javax.inject.Inject

class ToggleCodyFixedUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return try {
            codyRepository.toggleFixed(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
