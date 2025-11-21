package com.woowa.weatherfit.data.repository

import com.woowa.weatherfit.data.remote.api.WeatherApi
import com.woowa.weatherfit.domain.model.HourlyForecast
import com.woowa.weatherfit.domain.model.Weather
import com.woowa.weatherfit.domain.model.WeatherCondition
import com.woowa.weatherfit.domain.repository.WeatherRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    // TODO: Replace with actual API key from BuildConfig
    private val apiKey = "YOUR_API_KEY"

    override suspend fun getWeather(latitude: Double, longitude: Double): Result<Weather> {
        return try {
            val weatherResponse = weatherApi.getWeather(latitude, longitude, apiKey)
            val forecastResponse = weatherApi.getHourlyForecast(latitude, longitude, apiKey)

            val hourlyForecasts = forecastResponse.list.map { item ->
                val dateFormat = SimpleDateFormat("a h시", Locale.KOREAN)
                HourlyForecast(
                    hour = dateFormat.format(Date(item.dt * 1000)),
                    temperature = item.main.temp.toInt(),
                    weatherCondition = mapWeatherCondition(item.weather.firstOrNull()?.id ?: 800)
                )
            }

            Result.success(weatherResponse.toDomain(hourlyForecasts))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeatherByRegionName(regionName: String): Result<Weather> {
        // For simplicity, use predefined coordinates
        // In production, use geocoding API
        return getWeather(36.1194, 128.3445)
    }

    private fun mapWeatherCondition(weatherId: Int): WeatherCondition {
        return when (weatherId) {
            in 200..299 -> WeatherCondition.THUNDERSTORM
            in 300..399, in 500..599 -> WeatherCondition.RAIN
            in 600..699 -> WeatherCondition.SNOW
            in 700..799 -> WeatherCondition.FOG
            800 -> WeatherCondition.CLEAR
            801 -> WeatherCondition.PARTLY_CLOUDY
            else -> WeatherCondition.CLOUDY
        }
    }
}
