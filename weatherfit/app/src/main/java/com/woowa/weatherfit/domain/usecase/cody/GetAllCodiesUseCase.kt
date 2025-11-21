package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.repository.CodyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCodiesUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    operator fun invoke(): Flow<List<CodyWithClothes>> {
        return codyRepository.getAllCodiesWithClothes()
    }
}
