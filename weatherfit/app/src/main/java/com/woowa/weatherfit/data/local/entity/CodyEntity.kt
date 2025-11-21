package com.woowa.weatherfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.Season

@Entity(tableName = "codies")
data class CodyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val imageUrl: String?,
    val clothIds: String, // Comma-separated cloth IDs
    val season: String,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Cody = Cody(
        id = id,
        name = name,
        imageUrl = imageUrl,
        clothIds = clothIds.split(",").filter { it.isNotBlank() }.map { it.toLong() },
        season = Season.valueOf(season),
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(cody: Cody): CodyEntity = CodyEntity(
            id = cody.id,
            name = cody.name,
            imageUrl = cody.imageUrl,
            clothIds = cody.clothIds.joinToString(","),
            season = cody.season.name,
            createdAt = cody.createdAt
        )
    }
}
