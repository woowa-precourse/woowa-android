package com.woowa.weatherfit.domain.model

data class Cody(
    val id: Long = 0,
    val thumbnail: String? = null,
    val clothItems: List<CodyClothItem> = emptyList(),
    val category: Season,
    val isFixed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class CodyWithClothes(
    val cody: Cody,
    val clothes: List<Cloth>
)
