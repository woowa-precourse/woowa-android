package com.woowa.weatherfit.domain.usecase.cody

import com.woowa.weatherfit.domain.repository.CodyRepository
import javax.inject.Inject

class SyncCodiesUseCase @Inject constructor(
    private val codyRepository: CodyRepository
) {
    suspend operator fun invoke() {
        codyRepository.syncWithRemote()
    }
}
