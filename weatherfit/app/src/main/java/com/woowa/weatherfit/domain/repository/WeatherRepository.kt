package com.woowa.weatherfit.domain.repository

import com.woowa.weatherfit.domain.model.Weather

interface WeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double): Result<Weather>

    suspend fun getWeatherByRegionName(regionName: String): Result<Weather>
}
