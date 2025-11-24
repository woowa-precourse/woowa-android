package com.woowa.weatherfit.presentation.screen.home

import android.Manifest
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.woowa.weatherfit.R
import com.woowa.weatherfit.presentation.viewmodel.HomeViewModel
import com.woowa.weatherfit.ui.theme.CardShape
import com.woowa.weatherfit.ui.theme.WeatherGradientEnd
import com.woowa.weatherfit.ui.theme.WeatherGradientStart

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: androidx.navigation.NavHostController,
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

    // 한 번만 실행되도록 플래그 관리
    val hasInitialized = rememberSaveable { mutableStateOf(false) }

    // 화면이 처음 표시될 때 권한 요청
    LaunchedEffect(Unit) {
        if (!locationPermissionsState.allPermissionsGranted) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    // 권한이 승인되면 GPS 위치 가져오기 후 데이터 로드 시작 (한 번만)
    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted && !hasInitialized.value) {
            hasInitialized.value = true
            viewModel.updateLocationToCurrentPosition {
                viewModel.startObservingRegion()
            }
        }
    }

    // Navigation Result: 코디 수정/삭제 성공 시에만 데이터 새로고침
    val needsRefresh = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("needsRefresh", false)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(needsRefresh?.value) {
        if (needsRefresh?.value == true && hasInitialized.value) {
            viewModel.refresh()
            // 플래그 초기화
            navController.currentBackStackEntry?.savedStateHandle?.set("needsRefresh", false)
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator()
                Text(
                    text = "위치를 불러오는 중입니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        return
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Weather Section (전체 화면 배경)
        WeatherSection(
            location = uiState.regionName ?: "위치 선택",
            temperature = uiState.temperature?.toInt() ?: 0,
            weatherCondition = uiState.weatherCondition ?: "",
            season = uiState.currentSeason.displayName,
            hourlyWeather = uiState.hourlyWeather,
            debugGpsInfo = uiState.debugGpsInfo,
            onMenuClick = onNavigateToRegionSelect,
            modifier = Modifier.fillMaxSize()
        )

        // Recommended Cody Section (하단에 겹쳐서 배치)
        RecommendedOutfitSection(
            outfits = uiState.recommendedOutfits,
            onOutfitClick = onNavigateToCodyDetail,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun WeatherSection(
    location: String,
    temperature: Int,
    weatherCondition: String,
    season: String,
    hourlyWeather: List<com.woowa.weatherfit.presentation.state.HourlyWeatherItem>,
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

            Spacer(modifier = Modifier.height(8.dp))

            // Weather Icon
            Image(
                painter = painterResource(id = getWeatherIcon(weatherCondition)),
                contentDescription = weatherCondition,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "계절: $season", color = Color.White, fontSize = 14.sp)


            // 시간별 날씨 정보
            if (hourlyWeather.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HourlyWeatherSection(hourlyWeather = hourlyWeather)
            }

            Spacer(modifier = Modifier.height(24.dp))
            
        }
    }
}

@Composable
private fun HourlyWeatherSection(
    hourlyWeather: List<com.woowa.weatherfit.presentation.state.HourlyWeatherItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "시간별 날씨",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(hourlyWeather) { hourly ->
                HourlyWeatherItem(hourly = hourly)
            }
        }
    }
}

@Composable
private fun HourlyWeatherItem(
    hourly: com.woowa.weatherfit.presentation.state.HourlyWeatherItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(80.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 시간 표시 (timestamp에서 시간만 추출)
            val time = hourly.timestamp.substringAfter("T").substringBefore(":")
            Text(
                text = "${time}시",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 날씨 아이콘 표시
            Image(
                painter = painterResource(id = getWeatherIcon(hourly.weather)),
                contentDescription = hourly.weather,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 온도 표시
            Text(
                text = "${hourly.temperature.toInt()}°",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun RecommendedOutfitSection(
    outfits: List<com.woowa.weatherfit.presentation.state.OutfitRecommendation>,
    onOutfitClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (outfits.isEmpty()) {
        // 추천 코디가 없을 때 메시지 표시
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "추천 코디가 없습니다",
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    } else {
        LazyRow(
            modifier = modifier,
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

// 날씨 조건을 Drawable 리소스로 매핑하는 함수
@DrawableRes
private fun getWeatherIcon(weather: String): Int {
    return when (weather.lowercase()) {
        "thunderstorm" -> R.drawable.thunderstorm
        "drizzle" -> R.drawable.rain
        "rain" -> R.drawable.rain
        "snow" -> R.drawable.snow
        "atmosphere" -> R.drawable.atmosphere
        "clear" -> R.drawable.clear
        "clouds" -> R.drawable.clouds
        else -> R.drawable.clear // 기본값
    }
}
