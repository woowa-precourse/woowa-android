package com.woowa.weatherfit.presentation.screen.cloth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.presentation.viewmodel.ClothListViewModel
import com.woowa.weatherfit.ui.theme.CardShape
import com.woowa.weatherfit.ui.theme.ChipSelected
import com.woowa.weatherfit.ui.theme.ChipUnselected
import com.woowa.weatherfit.ui.theme.OnChipSelected
import com.woowa.weatherfit.ui.theme.OnChipUnselected
import com.woowa.weatherfit.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClothListScreen(
    viewModel: ClothListViewModel = hiltViewModel(),
    onNavigateToAddCloth: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("옷 관리") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddCloth,
                containerColor = Primary
            ) {
                Icon(Icons.Default.Add, "Add Cloth", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MainCategoryTabs(
                selectedCategory = uiState.selectedMainCategory,
                onCategorySelected = viewModel::selectMainCategory
            )

            SubCategoryChips(
                categories = uiState.availableSubCategories,
                selectedCategory = uiState.selectedSubCategory,
                onCategorySelected = viewModel::selectSubCategory
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                ClothesGrid(clothes = uiState.filteredClothes)
            }
        }
    }
}

@Composable
private fun MainCategoryTabs(
    selectedCategory: MainCategory,
    onCategorySelected: (MainCategory) -> Unit
) {
    val categories = MainCategory.entries
    TabRow(
        selectedTabIndex = categories.indexOf(selectedCategory),
        containerColor = Color.Transparent,
        contentColor = Primary
    ) {
        categories.forEach { category ->
            Tab(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                text = { Text(category.displayName) }
            )
        }
    }
}

@Composable
private fun SubCategoryChips(
    categories: List<SubCategory>,
    selectedCategory: SubCategory?,
    onCategorySelected: (SubCategory?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("전체") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ChipSelected,
                    selectedLabelColor = OnChipSelected,
                    containerColor = ChipUnselected,
                    labelColor = OnChipUnselected
                )
            )
        }
        items(categories) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
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
}

@Composable
private fun ClothesGrid(clothes: List<Cloth>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(clothes) { cloth ->
            Card(
                modifier = Modifier.size(110.dp),
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                AsyncImage(
                    model = cloth.imageUrl,
                    contentDescription = cloth.subCategory.displayName,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
