package com.woowa.weatherfit.domain.model

import androidx.compose.ui.graphics.Color

enum class ClothColor(
    val displayName: String,
    val colorValue: Long
) {
    WHITE("화이트", 0xFFFFFFFF),
    BLACK("블랙", 0xFF000000),
    GRAY("그레이", 0xFF808080),
    NAVY("네이비", 0xFF000080),
    BLUE("블루", 0xFF0000FF),
    SKY_BLUE("스카이블루", 0xFF87CEEB),
    RED("레드", 0xFFFF0000),
    PINK("핑크", 0xFFFFC0CB),
    ORANGE("오렌지", 0xFFFFA500),
    YELLOW("옐로우", 0xFFFFFF00),
    GREEN("그린", 0xFF008000),
    MINT("민트", 0xFF98FF98),
    PURPLE("퍼플", 0xFF800080),
    BROWN("브라운", 0xFF8B4513),
    BEIGE("베이지", 0xFFF5F5DC),
    KHAKI("카키", 0xFFC3B091),
    DENIM("데님", 0xFF1560BD),
    PATTERN("패턴", 0xFF808080); // 패턴/무늬가 있는 경우

    fun toComposeColor(): Color = Color(colorValue)
}
