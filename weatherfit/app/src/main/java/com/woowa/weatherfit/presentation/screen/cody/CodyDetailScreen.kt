package com.woowa.weatherfit.presentation.screen.cody

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.Cody
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.domain.model.TemperatureRange
import com.woowa.weatherfit.presentation.viewmodel.CodyDetailViewModel
import com.woowa.weatherfit.ui.theme.CardShape
import com.woowa.weatherfit.ui.theme.ChipSelected
import com.woowa.weatherfit.ui.theme.OnChipSelected
import com.woowa.weatherfit.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodyDetailScreen(
    viewModel: CodyDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToCodyDetail: (Long) -> Unit = {},
    onNavigateToEdit: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshCodyDetail()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    uiState.codyWithClothes?.cody?.category?.let { category ->
                        FilterChip(
                            selected = true,
                            onClick = { },
                            label = { Text(category.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ChipSelected,
                                selectedLabelColor = OnChipSelected
                            )
                        )
                    }
                    uiState.codyWithClothes?.cody?.let { cody ->
                        IconButton(onClick = { viewModel.toggleFixed() }) {
                            Icon(
                                imageVector = if (cody.isFixed) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                contentDescription = if (cody.isFixed) "고정 해제" else "고정",
                                tint = Primary
                            )
                        }
                        IconButton(onClick = { onNavigateToEdit(cody.id) }) {
                            Icon(Icons.Default.Edit, "Edit", tint = Primary)
                        }
                    }
                    IconButton(onClick = { viewModel.deleteCody(onNavigateBack) }) {
                        Icon(Icons.Default.Delete, "Delete", tint = Color.Red)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error!!,
                    color = Color.Red
                )
            }
        } else {
            val codyWithClothes = uiState.codyWithClothes ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("코디 데이터가 없습니다", color = Color.Gray)
                }
                return@Scaffold
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Main Cody Display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Display outfit thumbnail
                    AsyncImage(
                        model = codyWithClothes.cody.thumbnail,
                        contentDescription = "Outfit thumbnail",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                // Clothes Items List
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(codyWithClothes.clothes) { index, cloth ->
                        Card(
                            modifier = Modifier
                                .size(100.dp)
                                .clickable { viewModel.selectClothIndex(index) },
                            shape = CardShape,
                            colors = CardDefaults.cardColors(
                                containerColor = if (index == uiState.selectedClothIndex) Primary.copy(alpha = 0.1f) else Color.White
                            ),
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

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
