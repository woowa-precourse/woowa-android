package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyClothItem
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.repository.CodyRepository
import java.io.File
import javax.inject.Inject

class AddCodyUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    suspend operator fun invoke(
        thumbnail: File,
        clothItems: List<CodyClothItem>,
        category: Season
    ): Result<Cody> {
        return try {
            val createdCody = codyRepository.createOutfitRemote(thumbnail, clothItems, category)
            Result.success(createdCody)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
