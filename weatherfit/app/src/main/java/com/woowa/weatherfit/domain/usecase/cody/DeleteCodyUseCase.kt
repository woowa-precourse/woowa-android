package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.repository.CodyRepository
import javax.inject.Inject

class DeleteCodyUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    suspend operator fun invoke(codyId: Long) {
        codyRepository.deleteCodyById(codyId)
    }
}
