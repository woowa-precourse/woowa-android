package com.woowa.weatherfit.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodayResponse(
    @SerialName("region")
    val region: String,

    @SerialName("current")
    val current: CurrentWeather,

    @SerialName("hourly")
    val hourly: List<HourlyWeather>,

    @SerialName("outfits")
    val outfits: List<OutfitRecommendationResponse>
)

@Serializable
data class CurrentWeather(
    @SerialName("temperature")
    val temperature: Double,

    @SerialName("weather")
    val weather: String,

    @SerialName("timestamp")
    val timestamp: String
)

@Serializable
data class HourlyWeather(
    @SerialName("temperature")
    val temperature: Double,

    @SerialName("weather")
    val weather: String,

    @SerialName("timestamp")
    val timestamp: String
)

@Serializable
data class OutfitRecommendationResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("thumbnail")
    val thumbnail: String
)
