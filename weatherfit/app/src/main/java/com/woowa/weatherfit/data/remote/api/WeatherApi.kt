package com.woowa.weatherfit.data.remote.api

import com.woowa.weatherfit.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "kr"
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getHourlyForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "kr",
        @Query("cnt") count: Int = 8
    ): HourlyForecastResponse
}

@kotlinx.serialization.Serializable
data class HourlyForecastResponse(
    val list: List<ForecastItem>
)

@kotlinx.serialization.Serializable
data class ForecastItem(
    val dt: Long,
    val main: MainInfo,
    val weather: List<WeatherInfo>
)

@kotlinx.serialization.Serializable
data class MainInfo(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int
)

@kotlinx.serialization.Serializable
data class WeatherInfo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
