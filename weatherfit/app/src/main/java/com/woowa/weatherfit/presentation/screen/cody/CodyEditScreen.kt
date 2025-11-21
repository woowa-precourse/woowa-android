package com.woowa.weatherfit.presentation.screen.cody

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.presentation.viewmodel.CodyEditViewModel
import com.woowa.weatherfit.ui.theme.ButtonShape
import com.woowa.weatherfit.ui.theme.CardShape
import com.woowa.weatherfit.ui.theme.ChipSelected
import com.woowa.weatherfit.ui.theme.ChipUnselected
import com.woowa.weatherfit.ui.theme.OnChipSelected
import com.woowa.weatherfit.ui.theme.OnChipUnselected
import com.woowa.weatherfit.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodyEditScreen(
    viewModel: CodyEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) onNavigateBack()
    }

    Scaffold(topBar = { TopAppBar(title = { Text("코디 조합") }) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Selected Clothes Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.selectedClothes.isEmpty()) {
                    Text("옷을 선택해주세요", color = Color.Gray)
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(uiState.selectedClothes) { cloth ->
                            AsyncImage(
                                model = cloth.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.removeSelectedCloth(cloth) },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            // Season Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Season.entries.forEach { season ->
                    FilterChip(
                        selected = uiState.selectedSeason == season,
                        onClick = { viewModel.selectSeason(season) },
                        label = { Text(season.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ChipSelected,
                            selectedLabelColor = OnChipSelected,
                            containerColor = ChipUnselected,
                            labelColor = OnChipUnselected
                        )
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.saveCody() },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = ButtonShape,
                    enabled = !uiState.isSaving && uiState.selectedClothes.isNotEmpty()
                ) {
                    Text("저장")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Category Tabs
            val categories = MainCategory.entries
            TabRow(
                selectedTabIndex = categories.indexOf(uiState.selectedMainCategory),
                containerColor = Color.Transparent,
                contentColor = Primary
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = category == uiState.selectedMainCategory,
                        onClick = { viewModel.selectMainCategory(category) },
                        text = { Text(category.displayName) }
                    )
                }
            }

            // Sub Category Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedSubCategory == null,
                        onClick = { viewModel.selectSubCategory(null) },
                        label = { Text("전체") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ChipSelected,
                            selectedLabelColor = OnChipSelected,
                            containerColor = ChipUnselected,
                            labelColor = OnChipUnselected
                        )
                    )
                }
                items(uiState.availableSubCategories) { category ->
                    FilterChip(
                        selected = category == uiState.selectedSubCategory,
                        onClick = { viewModel.selectSubCategory(category) },
                        label = { Text(category.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ChipSelected,
                            selectedLabelColor = OnChipSelected,
                            containerColor = ChipUnselected,
                            labelColor = OnChipUnselected
                        )
                    )
                }
            }

            // Clothes Grid
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.filteredClothes) { cloth ->
                        val isSelected = uiState.selectedClothes.any { it.id == cloth.id }
                        Card(
                            modifier = Modifier
                                .size(100.dp)
                                .clickable { viewModel.toggleClothSelection(cloth) }
                                .then(if (isSelected) Modifier.border(3.dp, Primary, CardShape) else Modifier),
                            shape = CardShape,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            AsyncImage(
                                model = cloth.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}
