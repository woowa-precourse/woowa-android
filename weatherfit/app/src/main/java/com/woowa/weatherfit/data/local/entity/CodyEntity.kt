package com.woowa.weatherfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyClothItem
import com.woowa.weatherfit.domain.model.Season
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Entity(tableName = "codies")
data class CodyEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val thumbnail: String?,
    val clothItemsJson: String, // JSON-encoded List<CodyClothItem>
    val category: String,
    val isFixed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Cody = Cody(
        id = id,
        thumbnail = thumbnail,
        clothItems = try {
            Json.decodeFromString<List<CodyClothItem>>(clothItemsJson)
        } catch (e: Exception) {
            emptyList()
        },
        category = Season.valueOf(category),
        isFixed = isFixed,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(cody: Cody): CodyEntity = CodyEntity(
            id = cody.id,
            thumbnail = cody.thumbnail,
            clothItemsJson = Json.encodeToString(cody.clothItems),
            category = cody.category.name,
            isFixed = cody.isFixed,
            createdAt = cody.createdAt
        )
    }
}
