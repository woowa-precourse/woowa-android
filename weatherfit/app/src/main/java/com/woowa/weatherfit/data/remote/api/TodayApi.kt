package com.woowa.weatherfit.data.remote.api

import com.woowa.weatherfit.data.remote.dto.TodayResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TodayApi {
    @GET("today")
    suspend fun getTodayRecommendation(
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null,
        @Query("loc") locationName: String? = null
    ): TodayResponse
}
