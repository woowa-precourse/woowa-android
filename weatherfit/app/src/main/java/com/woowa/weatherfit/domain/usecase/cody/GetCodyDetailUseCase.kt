package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.repository.CodyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCodyDetailUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    operator fun invoke(codyId: Long): Flow<CodyWithClothes?> {
        return codyRepository.getCodyWithClothes(codyId)
    }
}
