package com.woowa.weatherfit.domain.model

enum class MainCategory(val displayName: String) {
    TOP("상의"),
    OUTER("아우터"),
    BOTTOM("하의"),
    ETC("기타")
}

enum class SubCategory(
    val displayName: String,
    val mainCategory: MainCategory
) {
    // 상의
    SHORT_SLEEVE("반팔", MainCategory.TOP),
    LONG_SLEEVE("긴팔", MainCategory.TOP),
    SHIRT("셔츠", MainCategory.TOP),
    KNIT("니트", MainCategory.TOP),
    HOODIE("후드티", MainCategory.TOP),
    SWEATSHIRT("맨투맨", MainCategory.TOP),
    VEST("조끼(베스트)", MainCategory.TOP),
    BLOUSE("블라우스", MainCategory.TOP),

    // 아우터
    CARDIGAN("가디건", MainCategory.OUTER),
    PADDING("패딩", MainCategory.OUTER),
    COAT("코트", MainCategory.OUTER),
    WINDBREAKER("바람막이", MainCategory.OUTER),
    JUMPER("점퍼/야상", MainCategory.OUTER),
    BLAZER("블레이저(자켓)", MainCategory.OUTER),

    // 하의
    LONG_PANTS("긴바지", MainCategory.BOTTOM),
    SHORT_PANTS("반바지", MainCategory.BOTTOM),
    SLACKS("슬랙스", MainCategory.BOTTOM),
    JEANS("청바지", MainCategory.BOTTOM),
    JOGGER("조거팬츠", MainCategory.BOTTOM),
    SKIRT("스커트", MainCategory.BOTTOM),

    // 기타
    DRESS("원피스", MainCategory.ETC),
    HAT("모자", MainCategory.ETC),
    SHOES("신발", MainCategory.ETC),
    HOODIE_ZIP("후드집업", MainCategory.ETC),
    TRAINING_SET("트레이닝복", MainCategory.ETC);

    companion object {
        fun getByMainCategory(mainCategory: MainCategory): List<SubCategory> {
            return entries.filter { it.mainCategory == mainCategory }
        }
    }
}
