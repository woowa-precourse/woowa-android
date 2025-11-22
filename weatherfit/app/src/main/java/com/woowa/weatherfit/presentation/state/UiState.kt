package com.woowa.weatherfit.presentation.state

import android.net.Uri
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.ClothColor
import com.woowa.weatherfit.domain.model.CodyClothItem
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.Region
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.model.TemperatureRange
import com.woowa.weatherfit.domain.model.Weather
import java.io.File

// Home
data class HomeUiState(
    val isLoading: Boolean = true,
    val weather: Weather? = null,
    val region: Region? = null,
    val recommendedCodies: List<CodyWithClothes> = emptyList(),
    val currentSeason: Season = Season.SPRING,
    val error: String? = null
)

// Cloth List
data class ClothListUiState(
    val isLoading: Boolean = true,
    val clothes: List<Cloth> = emptyList(),
    val filteredClothes: List<Cloth> = emptyList(),
    val selectedMainCategory: MainCategory = MainCategory.TOP,
    val selectedSubCategory: SubCategory? = null,
    val availableSubCategories: List<SubCategory> = SubCategory.getByMainCategory(MainCategory.TOP)
)

// Add Cloth
data class AddClothUiState(
    val imageUri: Uri? = null,
    val selectedMainCategory: MainCategory = MainCategory.TOP,
    val selectedSubCategory: SubCategory = SubCategory.SHORT_SLEEVE,
    val selectedTemperatureRange: TemperatureRange = TemperatureRange.WARM,
    val selectedColor: ClothColor? = null,
    val availableSubCategories: List<SubCategory> = SubCategory.getByMainCategory(MainCategory.TOP),
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

// Cody List
data class CodyListUiState(
    val isLoading: Boolean = true,
    val codies: List<CodyWithClothes> = emptyList(),
    val fixedCodies: List<CodyWithClothes> = emptyList(),
    val regularCodies: List<CodyWithClothes> = emptyList(),
    val isEditMode: Boolean = false
)

// Cody Edit
data class CodyEditUiState(
    val isLoading: Boolean = true,
    val allClothes: List<Cloth> = emptyList(),
    val filteredClothes: List<Cloth> = emptyList(),
    val selectedClothes: List<Cloth> = emptyList(),
    val clothItemsWithPosition: Map<Long, CodyClothItem> = emptyMap(),
    val selectedSeason: Season = Season.AUTUMN,
    val selectedMainCategory: MainCategory = MainCategory.TOP,
    val selectedSubCategory: SubCategory? = null,
    val availableSubCategories: List<SubCategory> = SubCategory.getByMainCategory(MainCategory.TOP),
    val thumbnailFile: File? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

// Cody Detail
data class CodyDetailUiState(
    val isLoading: Boolean = true,
    val codyWithClothes: CodyWithClothes? = null,
    val selectedClothIndex: Int = 0,
    val error: String? = null
)

// Region Select
data class RegionSelectUiState(
    val searchQuery: String = "",
    val regions: List<Region> = emptyList(),
    val selectedRegion: Region? = null,
    val saveSuccess: Boolean = false
)
