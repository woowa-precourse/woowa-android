package com.woowa.weatherfit.domain.usecase.today

import com.woowa.weatherfit.data.remote.dto.TodayResponse
import com.woowa.weatherfit.domain.repository.TodayRepository
import javax.inject.Inject

class GetTodayRecommendationUseCase @Inject constructor(
    private val todayRepository: TodayRepository
) {
    suspend operator fun invoke(
        latitude: Double?,
        longitude: Double?,
        locationName: String?
    ): Result<TodayResponse> {
        return todayRepository.getTodayRecommendation(
            latitude = latitude,
            longitude = longitude,
            locationName = locationName
        )
    }
}
