package com.woowa.weatherfit.domain.model

data class Weather(
    val temperature: Int,
    val minTemperature: Int,
    val maxTemperature: Int,
    val description: String,
    val weatherCondition: WeatherCondition,
    val humidity: Int,
    val windSpeed: Double,
    val location: String,
    val hourlyForecasts: List<HourlyForecast> = emptyList()
)

data class HourlyForecast(
    val hour: String,
    val temperature: Int,
    val weatherCondition: WeatherCondition
)

enum class WeatherCondition(val description: String, val iconName: String) {
    CLEAR("맑음", "clear"),
    PARTLY_CLOUDY("부분적으로 흐림", "partly_cloudy"),
    CLOUDY("흐림", "cloudy"),
    RAIN("비", "rain"),
    SNOW("눈", "snow"),
    THUNDERSTORM("천둥번개", "thunderstorm"),
    FOG("안개", "fog"),
    NIGHT_CLEAR("맑음(밤)", "night_clear"),
    NIGHT_CLOUDY("흐림(밤)", "night_cloudy")
}
