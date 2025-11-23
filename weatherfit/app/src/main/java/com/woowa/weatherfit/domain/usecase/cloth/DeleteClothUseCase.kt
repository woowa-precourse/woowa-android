package com.woowa.weatherfit.domain.usecase.cloth

import com.woowa.weatherfit.domain.repository.ClothRepository
import javax.inject.Inject

class DeleteClothUseCase @Inject constructor(
    private val clothRepository: ClothRepository
) {
    suspend operator fun invoke(clothId: Long): Result<Unit> {
        return clothRepository.deleteClothesFromServer(clothId)
    }
}
