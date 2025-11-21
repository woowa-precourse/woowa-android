package com.woowa.weatherfit.domain.usecase.region

import com.woowa.weatherfit.domain.model.Region
import com.woowa.weatherfit.domain.repository.RegionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSelectedRegionUseCase @Inject constructor(
    private val regionRepository: RegionRepository
) {
    operator fun invoke(): Flow<Region> {
        return regionRepository.getSelectedRegion()
    }
}
