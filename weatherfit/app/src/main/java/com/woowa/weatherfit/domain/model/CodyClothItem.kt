package com.woowa.weatherfit.domain.model

data class CodyClothItem(
    val clothId: Long,
    val x: Float = 0.5f,      // 0.0 ~ 1.0 (캔버스 비율 기준 x 좌표)
    val y: Float = 0.5f,      // 0.0 ~ 1.0 (캔버스 비율 기준 y 좌표)
    val scale: Float = 1.0f,  // 크기 배율
    val rotation: Float = 0f  // 회전 각도
)
