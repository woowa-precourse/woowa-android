package com.woowa.weatherfit.domain.usecase.location

import com.woowa.weatherfit.data.location.LocationData
import com.woowa.weatherfit.data.location.LocationService
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationService: LocationService
) {
    suspend operator fun invoke(): Result<LocationData> {
        return locationService.getCurrentLocation()
    }
}
