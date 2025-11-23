package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.repository.CodyRepository
import javax.inject.Inject

class GetAllCodiesUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    suspend operator fun invoke(): Result<Pair<List<Cody>, List<Cody>>> {
        return try {
            Result.success(codyRepository.getOutfitsRemote())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
