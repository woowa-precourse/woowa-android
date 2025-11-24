package com.woowa.weatherfit.data.repository

import com.woowa.weatherfit.data.remote.api.TodayApi
import com.woowa.weatherfit.data.remote.dto.TodayResponse
import com.woowa.weatherfit.domain.repository.TodayRepository
import javax.inject.Inject

class TodayRepositoryImpl @Inject constructor(
    private val todayApi: TodayApi
) : TodayRepository {

    override suspend fun getTodayRecommendation(
        latitude: Double?,
        longitude: Double?,
        locationName: String?
    ): Result<TodayResponse> = runCatching {
        todayApi.getTodayRecommendation(
            latitude = latitude,
            longitude = longitude,
            locationName = locationName
        )
    }
}
