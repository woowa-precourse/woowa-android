package com.woowa.weatherfit.domain.usecase.cloth

import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.repository.ClothRepository
import javax.inject.Inject

class AddClothUseCase @Inject constructor(
    private val clothRepository: ClothRepository
) {
    suspend operator fun invoke(cloth: Cloth): Long {
        return clothRepository.insertCloth(cloth)
    }
}
