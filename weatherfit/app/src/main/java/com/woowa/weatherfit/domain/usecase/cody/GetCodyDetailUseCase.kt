package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.repository.CodyRepository
import javax.inject.Inject

class GetCodyDetailUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    suspend operator fun invoke(codyId: Long): Result<Cody> {
        return try {
            Result.success(codyRepository.getOutfitDetailRemote(codyId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
