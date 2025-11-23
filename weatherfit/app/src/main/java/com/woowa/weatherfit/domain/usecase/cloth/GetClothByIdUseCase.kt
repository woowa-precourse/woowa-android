package com.woowa.weatherfit.domain.usecase.cloth

import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.repository.ClothRepository
import javax.inject.Inject

class GetClothByIdUseCase @Inject constructor(
    private val clothRepository: ClothRepository
) {
    suspend operator fun invoke(id: Long): Result<Cloth> {
        return clothRepository.fetchClothesDetailFromServer(id)
    }
}
