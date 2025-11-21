package com.woowa.weatherfit.domain.model

data class Cody(
    val id: Long = 0,
    val name: String = "",
    val imageUrl: String? = null,
    val clothIds: List<Long> = emptyList(),
    val season: Season,
    val createdAt: Long = System.currentTimeMillis()
)

data class CodyWithClothes(
    val cody: Cody,
    val clothes: List<Cloth>
)
