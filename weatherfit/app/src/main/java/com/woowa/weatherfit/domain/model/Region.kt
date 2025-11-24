package com.woowa.weatherfit.domain.model

data class Region(
    val id: Long = 0,
    val name: String,
    val province: Province,
    val latitude: Double,
    val longitude: Double
)

// Predefined regions
object Regions {
    val DEFAULT = Region(
        id = 1,
        name = "구미시",
        province = Province.GYEONGBUK,
        latitude = 36.1194,
        longitude = 128.3445
    )

    val LIST = listOf(
        DEFAULT,
        // 서울특별시
        Region(2, "서울특별시", Province.SEOUL, 37.5665, 126.9780),

        // 광역시
        Region(3, "부산광역시", Province.BUSAN, 35.1796, 129.0756),
        Region(4, "대구광역시", Province.DAEGU, 35.8714, 128.6014),
        Region(5, "인천광역시", Province.INCHEON, 37.4563, 126.7052),
        Region(6, "광주광역시", Province.GWANGJU, 35.1595, 126.8526),
        Region(7, "대전광역시", Province.DAEJEON, 36.3504, 127.3845),
        Region(8, "울산광역시", Province.ULSAN, 35.5384, 129.3114),
        Region(9, "세종특별자치시", Province.SEJONG, 36.4800, 127.2890),

        // 도 (도청 소재지)
        Region(10, "경기도", Province.GYEONGGI, 37.4138, 127.5183),
        Region(11, "강원특별자치도", Province.GANGWON, 37.8228, 128.1555),
        Region(12, "충청북도", Province.CHUNGBUK, 36.6357, 127.4912),
        Region(13, "충청남도", Province.CHUNGNAM, 36.5184, 126.8000),
        Region(14, "전북특별자치도", Province.JEONBUK, 35.8203, 127.1088),
        Region(15, "전라남도", Province.JEONNAM, 34.8161, 126.4629),
        Region(16, "경상북도", Province.GYEONGBUK, 36.4919, 128.8889),
        Region(17, "경상남도", Province.GYEONGNAM, 35.4606, 128.2132),
        Region(18, "제주특별자치도", Province.JEJU, 33.4996, 126.5312),

        // --- 경기도 (28개) ---
        Region(19, "수원시", Province.GYEONGGI, 37.2636, 127.0286),
        Region(20, "고양시", Province.GYEONGGI, 37.6584, 126.8320),
        Region(21, "용인시", Province.GYEONGGI, 37.2411, 127.1775),
        Region(22, "성남시", Province.GYEONGGI, 37.4200, 127.1265),
        Region(23, "부천시", Province.GYEONGGI, 37.5034, 126.7660),
        Region(24, "안산시", Province.GYEONGGI, 37.3219, 126.8309),
        Region(25, "안양시", Province.GYEONGGI, 37.3943, 126.9568),
        Region(26, "남양주시", Province.GYEONGGI, 37.6360, 127.2165),
        Region(27, "화성시", Province.GYEONGGI, 37.1990, 126.8314),
        Region(28, "평택시", Province.GYEONGGI, 36.9920, 127.1129),
        Region(29, "의정부시", Province.GYEONGGI, 37.7381, 127.0336),
        Region(30, "시흥시", Province.GYEONGGI, 37.3799, 126.8031),
        Region(31, "파주시", Province.GYEONGGI, 37.7599, 126.7800),
        Region(32, "김포시", Province.GYEONGGI, 37.6153, 126.7150),
        Region(33, "광명시", Province.GYEONGGI, 37.4781, 126.8643),
        Region(34, "광주시", Province.GYEONGGI, 37.4170, 127.2565),
        Region(35, "군포시", Province.GYEONGGI, 37.3614, 126.9350),
        Region(36, "이천시", Province.GYEONGGI, 37.2721, 127.4350),
        Region(37, "오산시", Province.GYEONGGI, 37.1499, 127.0770),
        Region(38, "하남시", Province.GYEONGGI, 37.5393, 127.2145),
        Region(39, "안성시", Province.GYEONGGI, 37.0080, 127.2791),
        Region(40, "의왕시", Province.GYEONGGI, 37.3444, 126.9683),
        Region(41, "양주시", Province.GYEONGGI, 37.7853, 127.0095),
        Region(42, "구리시", Province.GYEONGGI, 37.5942, 127.1290),
        Region(43, "포천시", Province.GYEONGGI, 37.8949, 127.2000),
        Region(44, "여주시", Province.GYEONGGI, 37.2983, 127.6377),
        Region(45, "동두천시", Province.GYEONGGI, 37.9034, 127.0605),
        Region(46, "과천시", Province.GYEONGGI, 37.4292, 126.9877),

        // --- 강원특별자치도 (7개) ---
        Region(47, "춘천시", Province.GANGWON, 37.8813, 127.7298),
        Region(48, "원주시", Province.GANGWON, 37.3417, 127.9202),
        Region(49, "강릉시", Province.GANGWON, 37.7519, 128.8761),
        Region(50, "동해시", Province.GANGWON, 37.5240, 129.1137),
        Region(51, "태백시", Province.GANGWON, 37.1640, 128.9856),
        Region(52, "속초시", Province.GANGWON, 38.2070, 128.5919),
        Region(53, "삼척시", Province.GANGWON, 37.4471, 129.1651),

        // --- 충청북도 (3개) ---
        Region(54, "청주시", Province.CHUNGBUK, 36.6424, 127.4887),
        Region(55, "충주시", Province.CHUNGBUK, 36.9910, 127.9250),
        Region(56, "제천시", Province.CHUNGBUK, 37.1366, 128.1908),

        // --- 충청남도 (8개) ---
        Region(57, "천안시", Province.CHUNGNAM, 36.8065, 127.1520),
        Region(58, "공주시", Province.CHUNGNAM, 36.4674, 127.1190),
        Region(59, "보령시", Province.CHUNGNAM, 36.3333, 126.6120),
        Region(60, "아산시", Province.CHUNGNAM, 36.7899, 127.0010),
        Region(61, "서산시", Province.CHUNGNAM, 36.7845, 126.4504),
        Region(62, "논산시", Province.CHUNGNAM, 36.1871, 127.0980),
        Region(63, "계룡시", Province.CHUNGNAM, 36.2748, 127.2488),
        Region(64, "당진시", Province.CHUNGNAM, 36.8894, 126.6457),

        // --- 전북특별자치도 (6개) ---
        Region(65, "전주시", Province.JEONBUK, 35.8242, 127.1480),
        Region(66, "군산시", Province.JEONBUK, 35.9674, 126.7365),
        Region(67, "익산시", Province.JEONBUK, 35.9483, 126.9570),
        Region(68, "정읍시", Province.JEONBUK, 35.5694, 126.8581),
        Region(69, "남원시", Province.JEONBUK, 35.4163, 127.3905),
        Region(70, "김제시", Province.JEONBUK, 35.8032, 126.8807),

        // --- 전라남도 (5개) ---
        Region(71, "목포시", Province.JEONNAM, 34.8118, 126.3922),
        Region(72, "여수시", Province.JEONNAM, 34.7604, 127.6622),
        Region(73, "순천시", Province.JEONNAM, 34.9481, 127.4890),
        Region(74, "나주시", Province.JEONNAM, 35.0154, 126.7108),
        Region(75, "광양시", Province.JEONNAM, 34.9401, 127.6958),

        // --- 경상북도 (9개 - 구미시는 DEFAULT로 이미 포함) ---
        Region(76, "포항시", Province.GYEONGBUK, 36.0190, 129.3435),
        Region(77, "경주시", Province.GYEONGBUK, 35.8562, 129.2247),
        Region(78, "김천시", Province.GYEONGBUK, 36.1398, 128.1136),
        Region(79, "안동시", Province.GYEONGBUK, 36.5684, 128.7294),
        Region(80, "영주시", Province.GYEONGBUK, 36.8250, 128.6240),
        Region(81, "영천시", Province.GYEONGBUK, 35.9733, 128.9389),
        Region(82, "상주시", Province.GYEONGBUK, 36.4108, 128.1597),
        Region(83, "문경시", Province.GYEONGBUK, 36.5860, 128.1840),
        Region(84, "경산시", Province.GYEONGBUK, 35.8250, 128.7415),

        // --- 경상남도 (8개) ---
        Region(85, "창원시", Province.GYEONGNAM, 35.2276, 128.6811),
        Region(86, "진주시", Province.GYEONGNAM, 35.1802, 128.1076),
        Region(87, "통영시", Province.GYEONGNAM, 34.8544, 128.4335),
        Region(88, "사천시", Province.GYEONGNAM, 35.0038, 128.0640),
        Region(89, "김해시", Province.GYEONGNAM, 35.2285, 128.8895),
        Region(90, "밀양시", Province.GYEONGNAM, 35.5037, 128.7461),
        Region(91, "거제시", Province.GYEONGNAM, 34.8806, 128.6217),
        Region(92, "양산시", Province.GYEONGNAM, 35.3350, 129.0370),

        // --- 제주특별자치도 (2개) ---
        Region(93, "제주시", Province.JEJU, 33.4996, 126.5312),
        Region(94, "서귀포시", Province.JEJU, 33.2541, 126.5601)
    )
}
