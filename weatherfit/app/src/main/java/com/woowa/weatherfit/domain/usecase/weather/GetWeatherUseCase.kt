package com.woowa.weatherfit.domain.usecase.weather

import com.woowa.weatherfit.domain.model.Weather
import com.woowa.weatherfit.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<Weather> {
        return weatherRepository.getWeather(latitude, longitude)
    }
}
