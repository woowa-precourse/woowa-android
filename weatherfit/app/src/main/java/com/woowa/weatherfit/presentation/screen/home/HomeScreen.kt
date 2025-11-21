package com.woowa.weatherfit.presentation.screen.home

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.domain.model.HourlyForecast
import com.woowa.weatherfit.domain.model.WeatherCondition
import com.woowa.weatherfit.presentation.viewmodel.HomeViewModel
import com.woowa.weatherfit.ui.theme.CardShape
import com.woowa.weatherfit.ui.theme.WeatherGradientEnd
import com.woowa.weatherfit.ui.theme.WeatherGradientStart

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToRegionSelect: () -> Unit,
    onNavigateToCodyDetail: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            .verticalScroll(rememberScrollState())
    ) {
        // Weather Section
        WeatherSection(
            location = uiState.region?.name ?: "위치 선택",
            temperature = uiState.weather?.temperature ?: 0,
            description = uiState.weather?.description ?: "",
            minTemp = uiState.weather?.minTemperature ?: 0,
            maxTemp = uiState.weather?.maxTemperature ?: 0,
            hourlyForecasts = uiState.weather?.hourlyForecasts ?: emptyList(),
            onMenuClick = onNavigateToRegionSelect
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recommended Cody Section
        RecommendedCodySection(
            codies = uiState.recommendedCodies,
            onCodyClick = onNavigateToCodyDetail
        )
    }
}

@Composable
private fun WeatherSection(
    location: String,
    temperature: Int,
    description: String,
    minTemp: Int,
    maxTemp: Int,
    hourlyForecasts: List<HourlyForecast>,
    onMenuClick: () -> Unit
) {
    Box(
        modifier = Modifier
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

            Text(text = description, color = Color.White, fontSize = 16.sp)
            Text(text = "최고:${maxTemp}° 최저:${minTemp}°", color = Color.White, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(24.dp))

            // Weather Tip
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "오후 8시쯤 청명한 상태가 예상됩니다. 돌풍의 풍속은 최대 4m/s입니다.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(hourlyForecasts.take(5)) { forecast ->
                            HourlyForecastItem(forecast)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HourlyForecastItem(forecast: HourlyForecast) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = forecast.hour, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
        Icon(
            imageVector = when (forecast.weatherCondition) {
                WeatherCondition.CLEAR, WeatherCondition.NIGHT_CLEAR -> Icons.Default.WbSunny
                else -> Icons.Default.Cloud
            },
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${forecast.temperature}°",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RecommendedCodySection(
    codies: List<CodyWithClothes>,
    onCodyClick: (Long) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(codies) { codyWithClothes ->
            CodyCard(
                codyWithClothes = codyWithClothes,
                onClick = { onCodyClick(codyWithClothes.cody.id) }
            )
        }
    }
}

@Composable
private fun CodyCard(
    codyWithClothes: CodyWithClothes,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .clickable(onClick = onClick),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            codyWithClothes.clothes.take(3).forEach { cloth ->
                AsyncImage(
                    model = cloth.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
