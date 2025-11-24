package com.woowa.weatherfit.presentation.screen.cody

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woowa.weatherfit.domain.model.CodyWithClothes
import com.woowa.weatherfit.presentation.common.CodyCard
import com.woowa.weatherfit.presentation.viewmodel.CodyListViewModel
import com.woowa.weatherfit.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodyListScreen(
    viewModel: CodyListViewModel = hiltViewModel(),
    onNavigateToCodyEdit: () -> Unit,
    onNavigateToCodyDetail: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 화면이 다시 보여질 때마다 리스트 새로고침
    val lifecycleOwner = LocalLifecycleOwner.current
    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshCodies()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
        },
        floatingActionButton = {
            if (!uiState.isEditMode) {
                FloatingActionButton(
                    onClick = onNavigateToCodyEdit,
                    containerColor = Primary
                ) {
                    Icon(Icons.Default.Add, "Add Cody", tint = Color.White)
                }
            }
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
            // Combine fixed and regular codies, fixed first
            val allCodies = uiState.fixedCodies + uiState.regularCodies

            if (allCodies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("코디를 추가해보세요", color = Color.Gray, fontSize = 16.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(allCodies) { codyWithClothes ->
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
