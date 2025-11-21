package com.woowa.weatherfit.domain.model

enum class TemperatureRange(
    val displayName: String,
    val minTemp: Int,
    val maxTemp: Int
) {
    HOT("더울때", 28, Int.MAX_VALUE),           // 28도 이상
    WARM("따뜻할때", 20, 27),                   // 20~27도
    COOL("선선할때", 12, 19),                   // 12~19도
    COLD("추울때", Int.MIN_VALUE, 11);          // 11도 이하

    companion object {
        fun fromTemperature(temp: Int): TemperatureRange {
            return entries.find { temp in it.minTemp..it.maxTemp } ?: COOL
        }
    }
}

// Season for Cody (based on wireframe)
enum class Season(val displayName: String) {
    SPRING("봄"),
    SUMMER("여름"),
    AUTUMN("가을"),
    WINTER("겨울");

    fun toTemperatureRange(): TemperatureRange {
        return when (this) {
            SPRING -> TemperatureRange.COOL
            SUMMER -> TemperatureRange.HOT
            AUTUMN -> TemperatureRange.WARM
            WINTER -> TemperatureRange.COLD
        }
    }

    companion object {
        fun fromTemperatureRange(range: TemperatureRange): Season {
            return when (range) {
                TemperatureRange.HOT -> SUMMER
                TemperatureRange.WARM -> AUTUMN
                TemperatureRange.COOL -> SPRING
                TemperatureRange.COLD -> WINTER
            }
        }
    }
}
