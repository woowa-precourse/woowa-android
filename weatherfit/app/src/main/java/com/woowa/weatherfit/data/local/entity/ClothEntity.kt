package com.woowa.weatherfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.ClothColor
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.model.TemperatureRange

@Entity(tableName = "clothes")
data class ClothEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imageUrl: String,
    val mainCategory: String,
    val subCategory: String,
    val temperatureRange: String,
    val color: String?,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Cloth = Cloth(
        id = id,
        imageUrl = imageUrl,
        mainCategory = MainCategory.valueOf(mainCategory),
        subCategory = SubCategory.valueOf(subCategory),
        temperatureRange = TemperatureRange.valueOf(temperatureRange),
        color = color?.let { ClothColor.valueOf(it) },
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(cloth: Cloth): ClothEntity = ClothEntity(
            id = cloth.id,
            imageUrl = cloth.imageUrl,
            mainCategory = cloth.mainCategory.name,
            subCategory = cloth.subCategory.name,
            temperatureRange = cloth.temperatureRange.name,
            color = cloth.color?.name,
            createdAt = cloth.createdAt
        )
    }
}
