package com.woowa.weatherfit.domain.usecase.region

import com.woowa.weatherfit.domain.model.Region
import com.woowa.weatherfit.domain.repository.RegionRepository
import javax.inject.Inject

class SearchRegionsUseCase @Inject constructor(
    private val regionRepository: RegionRepository
) {
    operator fun invoke(query: String): List<Region> {
        return regionRepository.searchRegions(query)
    }
}
