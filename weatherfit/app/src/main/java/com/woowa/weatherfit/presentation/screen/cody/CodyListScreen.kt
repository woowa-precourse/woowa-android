package com.woowa.weatherfit.presentation.screen.cody

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.presentation.common.CodyCard
import com.woowa.weatherfit.presentation.viewmodel.CodyListViewModel
import com.woowa.weatherfit.ui.theme.CardShape
import com.woowa.weatherfit.ui.theme.Primary
import com.woowa.weatherfit.ui.theme.PrimaryContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodyListScreen(
    viewModel: CodyListViewModel = hiltViewModel(),
    onNavigateToCodyEdit: () -> Unit,
    onNavigateToCodyDetail: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("모든 코디") },
                actions = {
                    TextButton(onClick = { viewModel.toggleEditMode() }) {
                        Text(if (uiState.isEditMode) "완료" else "편집", color = Primary)
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Add New Cody Button
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(16.dp)
                        .clickable(onClick = onNavigateToCodyEdit),
                    shape = CardShape,
                    colors = CardDefaults.cardColors(containerColor = PrimaryContainer)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(2.dp, Primary.copy(alpha = 0.3f), CardShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, "Add Cody", tint = Primary, modifier = Modifier.size(32.dp))
                    }
                }

                // Fixed Codies Section
                if (uiState.fixedCodies.isNotEmpty()) {
                    Text(
                        text = "고정된 코디",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(((uiState.fixedCodies.size / 2 + uiState.fixedCodies.size % 2) * 212).dp)
                    ) {
                        items(uiState.fixedCodies) { codyWithClothes ->
                            CodyCardWithEdit(
                                codyWithClothes = codyWithClothes,
                                isEditMode = uiState.isEditMode,
                                onClick = { onNavigateToCodyDetail(codyWithClothes.cody.id) },
                                onDelete = { viewModel.deleteCody(codyWithClothes.cody.id) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Regular Codies Section
                if (uiState.regularCodies.isNotEmpty()) {
                    Text(
                        text = "모든 코디",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(((uiState.regularCodies.size / 2 + uiState.regularCodies.size % 2) * 212).dp)
                    ) {
                        items(uiState.regularCodies) { codyWithClothes ->
                            CodyCardWithEdit(
                                codyWithClothes = codyWithClothes,
                                isEditMode = uiState.isEditMode,
                                onClick = { onNavigateToCodyDetail(codyWithClothes.cody.id) },
                                onDelete = { viewModel.deleteCody(codyWithClothes.cody.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CodyCardWithEdit(
    codyWithClothes: CodyWithClothes,
    isEditMode: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Box {
        CodyCard(
            codyWithClothes = codyWithClothes,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        // Show pin icon for fixed codies
        if (codyWithClothes.cody.isFixed && !isEditMode) {
            Icon(
                imageVector = Icons.Default.PushPin,
                contentDescription = "Fixed",
                tint = Primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(20.dp)
            )
        }

        if (isEditMode) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
            ) {
                Icon(Icons.Default.Close, "Delete", tint = Color.Red)
            }
        }
    }
}
