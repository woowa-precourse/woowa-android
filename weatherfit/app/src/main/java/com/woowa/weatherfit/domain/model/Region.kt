package com.woowa.weatherfit.domain.model

data class Region(
    val id: Long = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

// Predefined regions
object Regions {
    val DEFAULT = Region(
        id = 1,
        name = "구미시",
        latitude = 36.1194,
        longitude = 128.3445
    )

    val LIST = listOf(
        DEFAULT,
        Region(2, "서울특별시", 37.5665, 126.9780),
        Region(3, "부산광역시", 35.1796, 129.0756),
        Region(4, "대구광역시", 35.8714, 128.6014),
        Region(5, "인천광역시", 37.4563, 126.7052),
        Region(6, "광주광역시", 35.1595, 126.8526),
        Region(7, "대전광역시", 36.3504, 127.3845),
        Region(8, "울산광역시", 35.5384, 129.3114),
        Region(9, "세종특별자치시", 36.4800, 127.2890),
        Region(10, "경기도", 37.4138, 127.5183),
        Region(11, "강원도", 37.8228, 128.1555),
        Region(12, "충청북도", 36.6357, 127.4912),
        Region(13, "충청남도", 36.5184, 126.8000),
        Region(14, "전라북도", 35.8203, 127.1088),
        Region(15, "전라남도", 34.8161, 126.4629),
        Region(16, "경상북도", 36.4919, 128.8889),
        Region(17, "경상남도", 35.4606, 128.2132),
        Region(18, "제주특별자치도", 33.4996, 126.5312)
    )
}
