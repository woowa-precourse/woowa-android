package com.woowa.weatherfit.domain.model

data class Cloth(
    val id: Long = 0,
    val imageUrl: String,
    val mainCategory: MainCategory,
    val subCategory: SubCategory,
    val temperatureRange: TemperatureRange,
    val color: ClothColor? = null,
    val createdAt: Long = System.currentTimeMillis()
)
