package com.woowa.weatherfit.presentation.screen.region

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woowa.weatherfit.domain.model.Province
import com.woowa.weatherfit.domain.model.Region
import com.woowa.weatherfit.presentation.viewmodel.RegionSelectViewModel
import com.woowa.weatherfit.ui.theme.ButtonShape
import com.woowa.weatherfit.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionSelectScreen(
    viewModel: RegionSelectViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "지역 선택",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Two-column layout
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Left column: Province list
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "도/특별자치도",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.groupedRegions.keys.toList()) { province ->
                            ProvinceItem(
                                province = province,
                                isSelected = province == uiState.selectedProvince,
                                onClick = { viewModel.selectProvince(province) }
                            )
                        }
                    }
                }

                // Vertical divider
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = Color.LightGray
                )

                // Right column: Region list
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "시/군",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val regionsToShow = uiState.selectedProvince?.let { province ->
                            val allRegions = uiState.groupedRegions[province] ?: emptyList()
                            if (allRegions.size <= 1) {
                                // 광역시/특별시 - 그대로 표시
                                allRegions
                            } else {
                                // 도 - 도 이름과 같은 것은 제외
                                allRegions.filter { region ->
                                    region.name != province.displayName
                                }
                            }
                        } ?: emptyList()

                        items(regionsToShow) { region ->
                            RegionItem(
                                region = region,
                                isSelected = region == uiState.selectedRegion,
                                onClick = { viewModel.selectRegion(region) }
                            )
                        }
                    }
                }
            }

            // Save button at bottom
            Button(
                onClick = { viewModel.saveSelectedRegion() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = ButtonShape,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                enabled = uiState.selectedRegion != null
            ) {
                Text("저장하기", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun ProvinceItem(
    province: Province,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (isSelected) Color(0xFFE3F2FD) else Color.Transparent)
    ) {
        // Blue indicator bar on the left
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .background(Primary)
                    .align(Alignment.CenterStart)
            )
        }

        Text(
            text = province.displayName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = if (isSelected) 20.dp else 16.dp, end = 16.dp)
                .padding(vertical = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) Primary else Color.Black
        )
    }
}

@Composable
private fun RegionItem(
    region: Region,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = region.name,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        style = MaterialTheme.typography.bodyLarge,
        color = if (isSelected) Primary else Color.Black
    )
}
