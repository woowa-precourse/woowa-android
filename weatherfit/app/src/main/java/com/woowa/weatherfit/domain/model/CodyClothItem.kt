package com.woowa.weatherfit.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CodyClothItem(
    val id: Long,          // cloth ID
    val xCoord: Double = 0.5,   // x coordinate (0.0 ~ 1.0)
    val yCoord: Double = 0.5,   // y coordinate (0.0 ~ 1.0)
    val zIndex: Int = 0,        // layer order (z-index)
    val scale: Double = 1.0     // size scale
)
