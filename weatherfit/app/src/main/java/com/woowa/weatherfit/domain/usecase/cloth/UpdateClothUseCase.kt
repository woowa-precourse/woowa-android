package com.woowa.weatherfit.domain.usecase.cloth

import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.repository.ClothRepository
import javax.inject.Inject

class UpdateClothUseCase @Inject constructor(
    private val clothRepository: ClothRepository
) {
    suspend operator fun invoke(cloth: Cloth): Result<Cloth> {
        return clothRepository.updateClothesOnServer(
            clothesId = cloth.id,
            category = cloth.mainCategory,
            subCategory = cloth.subCategory
        ).onSuccess { serverCloth ->
            // 서버에 성공적으로 업데이트되면 로컬 DB도 업데이트
            clothRepository.updateCloth(serverCloth)
        }
    }
}
