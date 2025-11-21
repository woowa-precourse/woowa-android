package com.woowa.weatherfit.domain.usecase.cloth

import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.repository.ClothRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClothesByCategoryUseCase @Inject constructor(
    private val clothRepository: ClothRepository
) {
    fun byMainCategory(mainCategory: MainCategory): Flow<List<Cloth>> {
        return clothRepository.getClothesByMainCategory(mainCategory)
    }

    fun bySubCategory(subCategory: SubCategory): Flow<List<Cloth>> {
        return clothRepository.getClothesBySubCategory(subCategory)
    }
}
