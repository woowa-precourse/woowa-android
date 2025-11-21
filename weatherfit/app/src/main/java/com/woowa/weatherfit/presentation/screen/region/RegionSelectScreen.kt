package com.woowa.weatherfit.presentation.screen.region

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woowa.weatherfit.domain.model.Region
import com.woowa.weatherfit.presentation.viewmodel.RegionSelectViewModel
import com.woowa.weatherfit.ui.theme.ButtonShape
import com.woowa.weatherfit.ui.theme.Primary
import com.woowa.weatherfit.ui.theme.SearchBarShape

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
                title = { },
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("지역 검색") },
                leadingIcon = { Icon(Icons.Default.Search, "Search") },
                shape = SearchBarShape,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Primary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.regions) { region ->
                    Text(
                        text = region.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectRegion(region) }
                            .padding(vertical = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (region == uiState.selectedRegion) Primary else Color.Black
                    )
                    HorizontalDivider(color = Color.LightGray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveSelectedRegion() },
                modifier = Modifier
                    .fillMaxWidth()
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
