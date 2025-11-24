package com.woowa.weatherfit.domain.repository

import com.woowa.weatherfit.data.remote.dto.TodayResponse

interface TodayRepository {
    suspend fun getTodayRecommendation(
        latitude: Double?,
        longitude: Double?,
        locationName: String?
    ): Result<TodayResponse>
}
