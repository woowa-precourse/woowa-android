package com.woowa.weatherfit.domain.usecase.cloth

import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.repository.ClothRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllClothesUseCase @Inject constructor(
    private val clothRepository: ClothRepository
) {
    operator fun invoke(): Flow<List<Cloth>> {
        return clothRepository.getAllClothes()
    }
}
