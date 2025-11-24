package com.woowa.weatherfit.data.remote.dto

import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyClothItem
import com.woowa.weatherfit.domain.model.Season
import kotlinx.serialization.Serializable

@Serializable
data class ClothesRequest(
    val id: Long,
    val xCoord: Double,
    val yCoord: Double,
    val zIndex: Int,
    val scale: Double
)

@Serializable
data class CreateOutfitRequest(
    val clothes: List<ClothesRequest>,
    val category: String
)

@Serializable
data class UpdateOutfitRequest(
    val clothes: List<ClothesRequest>,
    val category: String
)

@Serializable
data class OutfitClothesResponse(
    val id: Long,
    val image: String,
    val xCoord: Double,
    val yCoord: Double,
    val zIndex: Int,
    val scale: Double
)

@Serializable
data class OutfitResponse(
    val id: Long,
    val clothes: List<OutfitClothesResponse>,
    val category: String,
    val thumbnail: String
)

@Serializable
data class OutfitsListItem(
    val id: Long,
    val thumbnail: String
)

@Serializable
data class OutfitsListResponse(
    val fixedOutfits: List<OutfitsListItem>,
    val outfits: List<OutfitsListItem>
)

@Serializable
data class OutfitDetailClothesResponse(
    val id: Long,
    val image: String
)

@Serializable
data class OutfitDetailResponse(
    val id: Long,
    val thumbnail: String,
    val category: String,
    val clothes: List<OutfitClothesResponse>
)

@Serializable
data class ToggleFixedResponse(
    val id: Long,
    val fixed: Boolean
)

fun CodyClothItem.toClothesRequest() = ClothesRequest(
    id = id,
    xCoord = xCoord,
    yCoord = yCoord,
    zIndex = zIndex,
    scale = scale
)

fun OutfitClothesResponse.toCodyClothItem() = CodyClothItem(
    id = id,
    xCoord = xCoord,
    yCoord = yCoord,
    zIndex = zIndex,
    scale = scale
)

fun OutfitResponse.toCody(isFixed: Boolean = false) = Cody(
    id = id,
    thumbnail = thumbnail,
    clothItems = clothes.map { it.toCodyClothItem() },
    category = Season.valueOf(category.uppercase()),
    isFixed = isFixed
)

fun OutfitsListItem.toCody(isFixed: Boolean = false) = Cody(
    id = id,
    thumbnail = thumbnail,
    clothItems = emptyList(),
    category = Season.SPRING,
    isFixed = isFixed
)
