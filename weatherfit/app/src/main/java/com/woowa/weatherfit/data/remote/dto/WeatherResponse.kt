package com.woowa.weatherfit.data.remote.dto

import com.woowa.weatherfit.domain.model.HourlyForecast
import com.woowa.weatherfit.domain.model.Weather
import com.woowa.weatherfit.domain.model.WeatherCondition
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val coord: Coord,
    val weather: List<WeatherDto>,
    val main: Main,
    val wind: Wind,
    val name: String
) {
    fun toDomain(hourlyForecasts: List<HourlyForecast> = emptyList()): Weather {
        val weatherDto = weather.firstOrNull()
        return Weather(
            temperature = main.temp.toInt(),
            minTemperature = main.temp_min.toInt(),
            maxTemperature = main.temp_max.toInt(),
            description = weatherDto?.description ?: "",
            weatherCondition = weatherDto?.toWeatherCondition() ?: WeatherCondition.CLEAR,
            humidity = main.humidity,
            windSpeed = wind.speed,
            location = name,
            hourlyForecasts = hourlyForecasts
        )
    }
}

@Serializable
data class Coord(
    val lon: Double,
    val lat: Double
)

@Serializable
data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) {
    fun toWeatherCondition(): WeatherCondition {
        return when {
            icon.endsWith("n") -> {
                when (id) {
                    in 200..299 -> WeatherCondition.THUNDERSTORM
                    in 300..399, in 500..599 -> WeatherCondition.RAIN
                    in 600..699 -> WeatherCondition.SNOW
                    in 700..799 -> WeatherCondition.FOG
                    800 -> WeatherCondition.NIGHT_CLEAR
                    else -> WeatherCondition.NIGHT_CLOUDY
                }
            }
            else -> {
                when (id) {
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
    }
}

@Serializable
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

@Serializable
data class Wind(
    val speed: Double,
    val deg: Int = 0
)
