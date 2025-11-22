package com.woowa.weatherfit.data.remote.dto

import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.ClothColor
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.model.TemperatureRange
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClothesDetailResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("image")
    val image: String,

    @SerialName("category")
    val category: String,

    @SerialName("category_sub")
    val categorySub: String
) {
    fun toDomain(): Cloth {
        return Cloth(
            id = id,
            imageUrl = image,
            mainCategory = MainCategory.valueOf(category.uppercase()),
            subCategory = SubCategory.valueOf(categorySub.uppercase()),
            temperatureRange = TemperatureRange.COOL,
            color = null,
            createdAt = System.currentTimeMillis()
        )
    }
}

@Serializable
data class ClothesListResponse(
    @SerialName("clothes")
    val clothes: List<ClothesItem>,

    @SerialName("category")
    val category: String,

    @SerialName("category_sub")
    val categorySub: String
) {
    @Serializable
    data class ClothesItem(
        @SerialName("id")
        val id: Long,

        @SerialName("image")
        val image: String
    )

    fun toDomain(): List<Cloth> {
        return clothes.map { item ->
            Cloth(
                id = item.id,
                imageUrl = item.image,
                mainCategory = MainCategory.valueOf(category.uppercase()),
                subCategory = if (categorySub.isNotEmpty()) {
                    SubCategory.valueOf(categorySub.uppercase())
                } else {
                    // 전체 조회인 경우 기본값 사용
                    SubCategory.entries.first { it.mainCategory.name.equals(category, ignoreCase = true) }
                },
                temperatureRange = TemperatureRange.COOL,
                color = null,
                createdAt = System.currentTimeMillis()
            )
        }
    }
}
