package com.woowa.weatherfit.domain.usecase.region

import com.woowa.weatherfit.domain.model.Region
import com.woowa.weatherfit.domain.repository.RegionRepository
import javax.inject.Inject

class SetSelectedRegionUseCase @Inject constructor(
    private val regionRepository: RegionRepository
) {
    suspend operator fun invoke(region: Region) {
        regionRepository.setSelectedRegion(region)
    }
}
