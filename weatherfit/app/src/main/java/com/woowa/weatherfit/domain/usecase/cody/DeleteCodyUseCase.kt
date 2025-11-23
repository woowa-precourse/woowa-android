package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.repository.CodyRepository
import javax.inject.Inject

class DeleteCodyUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    suspend operator fun invoke(codyId: Long): Result<Unit> {
        return try {
            codyRepository.deleteOutfitRemote(codyId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
