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
import androidx.compose.material.icons.filled.MoreVert
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
    onNavigateToCodyDetail: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dummyCodies = getDummyCodiesForDetail()

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
                    uiState.codyWithClothes?.cody?.season?.let { season ->
                        FilterChip(
                            selected = true,
                            onClick = { },
                            label = { Text(season.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ChipSelected,
                                selectedLabelColor = OnChipSelected
                            )
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, "More")
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
        } else {
            // 현재 codyId에 해당하는 더미 데이터 찾기
            val currentCodyId = viewModel.currentCodyId
            val dummyCodyWithClothes = dummyCodies.find { it.cody.id == currentCodyId } ?: dummyCodies.firstOrNull()

            val codyWithClothes = uiState.codyWithClothes ?: dummyCodyWithClothes ?: return@Scaffold

            // 이전/다음 코디 ID 계산
            val currentIndex = dummyCodies.indexOfFirst { it.cody.id == codyWithClothes.cody.id }
            val prevCodyId = if (currentIndex > 0) dummyCodies[currentIndex - 1].cody.id else dummyCodies.last().cody.id
            val nextCodyId = if (currentIndex < dummyCodies.size - 1) dummyCodies[currentIndex + 1].cody.id else dummyCodies.first().cody.id

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
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp),
                        shape = CardShape,
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 왼쪽 열
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    codyWithClothes.clothes.filterIndexed { index, _ -> index % 2 == 0 }.forEach { cloth ->
                                        AsyncImage(
                                            model = cloth.imageUrl,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                                // 오른쪽 열
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    codyWithClothes.clothes.filterIndexed { index, _ -> index % 2 == 1 }.forEach { cloth ->
                                        AsyncImage(
                                            model = cloth.imageUrl,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            onNavigateToCodyDetail(prevCodyId)
                        }) {
                            Icon(Icons.Default.ChevronLeft, "Previous", tint = Color.Gray, modifier = Modifier.size(48.dp))
                        }
                        IconButton(onClick = {
                            onNavigateToCodyDetail(nextCodyId)
                        }) {
                            Icon(Icons.Default.ChevronRight, "Next", tint = Color.Gray, modifier = Modifier.size(48.dp))
                        }
                    }
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

// 더미 데이터 생성 함수
fun getDummyCodiesForDetail(): List<CodyWithClothes> {
    return listOf(
        CodyWithClothes(
            cody = Cody(id = 1, name = "코디 1", season = Season.SPRING),
            clothes = listOf(
                Cloth(id = 1, imageUrl = "https://picsum.photos/200/300?random=1", mainCategory = MainCategory.TOP, subCategory = SubCategory.LONG_SLEEVE, temperatureRange = TemperatureRange.COOL),
                Cloth(id = 2, imageUrl = "https://picsum.photos/200/300?random=2", mainCategory = MainCategory.BOTTOM, subCategory = SubCategory.LONG_PANTS, temperatureRange = TemperatureRange.COOL),
                Cloth(id = 3, imageUrl = "https://picsum.photos/200/300?random=3", mainCategory = MainCategory.ETC, subCategory = SubCategory.SHOES, temperatureRange = TemperatureRange.COOL)
            )
        ),
        CodyWithClothes(
            cody = Cody(id = 2, name = "코디 2", season = Season.SUMMER),
            clothes = listOf(
                Cloth(id = 4, imageUrl = "https://picsum.photos/200/300?random=4", mainCategory = MainCategory.TOP, subCategory = SubCategory.SHORT_SLEEVE, temperatureRange = TemperatureRange.HOT),
                Cloth(id = 5, imageUrl = "https://picsum.photos/200/300?random=5", mainCategory = MainCategory.BOTTOM, subCategory = SubCategory.SHORT_PANTS, temperatureRange = TemperatureRange.HOT),
                Cloth(id = 6, imageUrl = "https://picsum.photos/200/300?random=6", mainCategory = MainCategory.ETC, subCategory = SubCategory.SHOES, temperatureRange = TemperatureRange.HOT)
            )
        ),
        CodyWithClothes(
            cody = Cody(id = 3, name = "코디 3", season = Season.AUTUMN),
            clothes = listOf(
                Cloth(id = 7, imageUrl = "https://picsum.photos/200/300?random=7", mainCategory = MainCategory.TOP, subCategory = SubCategory.LONG_SLEEVE, temperatureRange = TemperatureRange.COOL),
                Cloth(id = 8, imageUrl = "https://picsum.photos/200/300?random=8", mainCategory = MainCategory.OUTER, subCategory = SubCategory.BLAZER, temperatureRange = TemperatureRange.COOL),
                Cloth(id = 9, imageUrl = "https://picsum.photos/200/300?random=9", mainCategory = MainCategory.BOTTOM, subCategory = SubCategory.LONG_PANTS, temperatureRange = TemperatureRange.COOL),
                Cloth(id = 10, imageUrl = "https://picsum.photos/200/300?random=10", mainCategory = MainCategory.ETC, subCategory = SubCategory.SHOES, temperatureRange = TemperatureRange.COOL)
            )
        )
    )
}
