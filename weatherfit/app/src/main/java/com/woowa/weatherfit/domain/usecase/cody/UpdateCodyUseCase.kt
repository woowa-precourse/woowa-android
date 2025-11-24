package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyClothItem
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.repository.CodyRepository
import java.io.File
import javax.inject.Inject

class UpdateCodyUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    suspend operator fun invoke(
        id: Long,
        thumbnail: File,
        clothItems: List<CodyClothItem>,
        category: Season
    ): Result<Cody> {
        return try {
            val updatedCody = codyRepository.updateOutfitRemote(id, thumbnail, clothItems, category)
            Result.success(updatedCody)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
