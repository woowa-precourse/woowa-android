package com.woowa.weatherfit.domain.usecase.cloth

import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.repository.ClothRepository
import javax.inject.Inject

class UpdateClothUseCase @Inject constructor(
    private val clothRepository: ClothRepository
) {
    suspend operator fun invoke(cloth: Cloth) {
        clothRepository.updateCloth(cloth)
    }
}
