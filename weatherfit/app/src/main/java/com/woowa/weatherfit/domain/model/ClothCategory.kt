package com.woowa.weatherfit.domain.model

enum class MainCategory(val displayName: String) {
    TOP("상의"),
    OUTER("아우터"),
    BOTTOM("하의"),
    SHOES("신발"),
    ACCESSORY("액세서리")
}

enum class SubCategory(
    val displayName: String,
    val mainCategory: MainCategory,
    val serverValue: String
) {
    // 상의
    SHORT_SLEEVE("반팔", MainCategory.TOP, "SHORT_SLEEVE_TEE"),
    LONG_SLEEVE("긴팔", MainCategory.TOP, "LONG_SLEEVE_TEE"),
    SHIRT("셔츠", MainCategory.TOP, "SHIRT"),
    KNIT("니트", MainCategory.TOP, "KNIT"),
    HOODIE("후드티", MainCategory.TOP, "HOODIE"),
    SWEATSHIRT("맨투맨", MainCategory.TOP, "SWEATSHIRT"),
    VEST("조끼(베스트)", MainCategory.TOP, "VEST"),
    BLOUSE("블라우스", MainCategory.TOP, "BLOUSE"),

    // 아우터
    CARDIGAN("가디건", MainCategory.OUTER, "CARDIGAN"),
    PADDING("패딩", MainCategory.OUTER, "PADDED_JACKET"),
    COAT("코트", MainCategory.OUTER, "COAT"),
    WINDBREAKER("바람막이", MainCategory.OUTER, "WINDBREAKER"),
    JUMPER("점퍼/야상", MainCategory.OUTER, "JUMPER"),
    BLAZER("블레이저(자켓)", MainCategory.OUTER, "BLAZER"),
    ZIP_UP_HOODIE("후드집업", MainCategory.OUTER, "ZIP_UP_HOODIE"),

    // 하의
    LONG_PANTS("긴바지", MainCategory.BOTTOM, "LONG_PANTS"),
    SHORT_PANTS("반바지", MainCategory.BOTTOM, "SHORTS"),
    SLACKS("슬랙스", MainCategory.BOTTOM, "SLACKS"),
    JEANS("청바지", MainCategory.BOTTOM, "JEANS"),
    JOGGER("조거팬츠", MainCategory.BOTTOM, "JOGGER_PANTS"),
    SKIRT("스커트", MainCategory.BOTTOM, "SKIRT"),

    // 신발
    SHOES("신발", MainCategory.SHOES, "SHOES"),

    // 액세서리
    DRESS("원피스", MainCategory.ACCESSORY, "DRESS"),
    HAT("모자", MainCategory.ACCESSORY, "HAT"),
    TRAINING_SET("트레이닝복", MainCategory.ACCESSORY, "TRACKSUIT");

    companion object {
        fun getByMainCategory(mainCategory: MainCategory): List<SubCategory> {
            return entries.filter { it.mainCategory == mainCategory }
        }

        fun fromServerValue(serverValue: String): SubCategory? {
            return entries.find { it.serverValue == serverValue }
        }
    }
}
