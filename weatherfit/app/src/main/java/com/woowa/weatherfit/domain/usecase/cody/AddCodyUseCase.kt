package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.repository.CodyRepository
import javax.inject.Inject

class AddCodyUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    suspend operator fun invoke(cody: Cody): Long {
        return codyRepository.insertCody(cody)
    }
}
