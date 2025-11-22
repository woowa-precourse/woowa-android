package com.woowa.weatherfit.domain.usecase.cloth

import com.woowa.weatherfit.domain.repository.ClothRepository
import javax.inject.Inject

class DeleteClothUseCase @Inject constructor(
    private val clothRepository: ClothRepository
) {
    suspend operator fun invoke(clothId: Long): Result<Unit> {
        return clothRepository.deleteClothesFromServer(clothId)
            .onSuccess {
                // 서버에서 성공적으로 삭제되면 로컬 DB에서도 삭제
                clothRepository.deleteClothById(clothId)
            }
    }
}
