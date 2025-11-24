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

        /**
         * 온도와 월 정보를 함께 고려하여 계절을 판단합니다.
         * 봄(10-15도 중심)과 가을(12-24도 범위)이 겹치는 구간은 월로 구분합니다.
         *
         * @param temp 현재 온도
         * @param month 현재 월 (1-12)
         * @return 해당하는 TemperatureRange
         */
        fun fromTemperatureAndMonth(temp: Int, month: Int): TemperatureRange {
            return when {
                temp >= 25 -> HOT // 25도 이상: 확실히 여름
                temp < 10 -> COLD // 10도 미만: 확실히 겨울
                temp in 10..24 -> { // 중간 온도: 월로 봄/가을 구분
                    when (month) {
                        3, 4, 5 -> COOL // 봄
                        9, 10, 11 -> WARM // 가을
                        6, 7, 8 -> HOT // 초여름
                        12, 1, 2 -> COLD // 초겨울
                        else -> COOL // fallback
                    }
                }
                else -> COOL // fallback
            }
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
