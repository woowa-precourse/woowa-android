package com.woowa.weatherfit.presentation.screen.home

import android.Manifest
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.woowa.weatherfit.presentation.viewmodel.HomeViewModel
import com.woowa.weatherfit.ui.theme.CardShape
import com.woowa.weatherfit.ui.theme.WeatherGradientEnd
import com.woowa.weatherfit.ui.theme.WeatherGradientStart

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToRegionSelect: () -> Unit,
    onNavigateToCodyDetail: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 위치 권한 요청
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // GPS 위치를 한 번만 가져왔는지 체크 (앱 재시작하면 리셋됨)
    val hasLocationBeenFetched = rememberSaveable { mutableStateOf(false) }

    // 화면이 처음 표시될 때 권한 요청
    LaunchedEffect(Unit) {
        if (!locationPermissionsState.allPermissionsGranted) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    // 권한이 승인되면 현재 위치로 업데이트 (한 번만)
    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted && !hasLocationBeenFetched.value) {
            viewModel.updateLocationToCurrentPosition()
            hasLocationBeenFetched.value = true
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Weather Section
        WeatherSection(
            location = uiState.regionName ?: "위치 선택",
            temperature = uiState.temperature?.toInt() ?: 0,
            weatherCondition = uiState.weatherCondition ?: "",
            season = uiState.currentSeason.displayName,
            debugGpsInfo = uiState.debugGpsInfo,
            onMenuClick = onNavigateToRegionSelect,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Recommended Cody Section
        RecommendedOutfitSection(
            outfits = uiState.recommendedOutfits,
            onOutfitClick = onNavigateToCodyDetail
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun WeatherSection(
    location: String,
    temperature: Int,
    weatherCondition: String,
    season: String,
    debugGpsInfo: String?,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WeatherGradientStart, WeatherGradientEnd)
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }

            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(text = "집", color = Color.White, fontSize = 12.sp)
            }

            Text(
                text = location,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Temperature
            Text(
                text = "${temperature}°",
                color = Color.White,
                fontSize = 80.sp,
                fontWeight = FontWeight.Light
            )

            Text(text = weatherCondition, color = Color.White, fontSize = 16.sp)
            Text(text = "계절: $season", color = Color.White, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(24.dp))

            // Weather Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "오늘의 날씨에 맞는 코디를 추천해드립니다.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // 디버그 GPS 정보 표시
                    if (debugGpsInfo != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🔍 디버그: $debugGpsInfo",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendedOutfitSection(
    outfits: List<com.woowa.weatherfit.presentation.state.OutfitRecommendation>,
    onOutfitClick: (Long) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(outfits) { outfit ->
            OutfitThumbnailCard(
                outfit = outfit,
                onClick = { onOutfitClick(outfit.id) },
                modifier = Modifier
                    .width(150.dp)
                    .height(200.dp)
            )
        }
    }
}

@Composable
private fun OutfitThumbnailCard(
    outfit: com.woowa.weatherfit.presentation.state.OutfitRecommendation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = CardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = outfit.thumbnail,
            contentDescription = "Outfit ${outfit.id}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
