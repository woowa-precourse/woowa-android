package com.woowa.weatherfit.presentation.screen.cody

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.math.roundToInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ComposeView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.model.MainCategory
import com.woowa.weatherfit.domain.model.Season
import com.woowa.weatherfit.domain.model.SubCategory
import com.woowa.weatherfit.presentation.viewmodel.CodyEditViewModel
import com.woowa.weatherfit.ui.theme.ButtonShape
import com.woowa.weatherfit.ui.theme.CardShape
import com.woowa.weatherfit.ui.theme.ChipSelected
import com.woowa.weatherfit.ui.theme.ChipUnselected
import com.woowa.weatherfit.ui.theme.OnChipSelected
import com.woowa.weatherfit.ui.theme.OnChipUnselected
import com.woowa.weatherfit.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodyEditScreen(
    viewModel: CodyEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var canvasView by remember { mutableStateOf<View?>(null) }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) onNavigateBack()
    }

    Scaffold(topBar = { TopAppBar(title = { Text("코디 조합") }) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Draggable Canvas for Outfit Composition
            var selectedClothId by remember { mutableStateOf<Long?>(null) }

            DraggableOutfitCanvas(
                selectedClothes = uiState.selectedClothes,
                clothItemsWithPosition = uiState.clothItemsWithPosition,
                selectedClothId = selectedClothId,
                onSelectCloth = { clothId -> selectedClothId = clothId },
                onUpdatePosition = { clothId, x, y, scale ->
                    viewModel.updateClothPosition(clothId, x, y, scale)
                },
                onUpdateZIndex = { clothId, zIndex ->
                    viewModel.updateClothZIndex(clothId, zIndex)
                },
                onRemoveCloth = { cloth ->
                    viewModel.removeSelectedCloth(cloth)
                    if (selectedClothId == cloth.id) selectedClothId = null
                },
                onViewReady = { view -> canvasView = view },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(16.dp)
            )

            // Season Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Season.entries.forEach { season ->
                    FilterChip(
                        selected = uiState.selectedSeason == season,
                        onClick = { viewModel.selectSeason(season) },
                        label = { Text(season.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ChipSelected,
                            selectedLabelColor = OnChipSelected,
                            containerColor = ChipUnselected,
                            labelColor = OnChipUnselected
                        )
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        scope.launch {
                            canvasView?.let { view ->
                                val bitmap = captureViewToBitmap(view)
                                val file = saveBitmapToFile(context, bitmap)
                                viewModel.setThumbnail(file)
                                viewModel.saveCody()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = ButtonShape,
                    enabled = !uiState.isSaving && uiState.selectedClothes.isNotEmpty()
                ) {
                    Text("저장")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Category Tabs
            val categories = MainCategory.entries
            TabRow(
                selectedTabIndex = categories.indexOf(uiState.selectedMainCategory),
                containerColor = Color.Transparent,
                contentColor = Primary
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = category == uiState.selectedMainCategory,
                        onClick = { viewModel.selectMainCategory(category) },
                        text = { Text(category.displayName) }
                    )
                }
            }

            // Sub Category Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedSubCategory == null,
                        onClick = { viewModel.selectSubCategory(null) },
                        label = { Text("전체") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ChipSelected,
                            selectedLabelColor = OnChipSelected,
                            containerColor = ChipUnselected,
                            labelColor = OnChipUnselected
                        )
                    )
                }
                items(uiState.availableSubCategories) { category ->
                    FilterChip(
                        selected = category == uiState.selectedSubCategory,
                        onClick = { viewModel.selectSubCategory(category) },
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

            // Clothes Grid
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.filteredClothes) { cloth ->
                        val isSelected = uiState.selectedClothes.any { it.id == cloth.id }
                        Card(
                            modifier = Modifier
                                .size(100.dp)
                                .clickable { viewModel.toggleClothSelection(cloth) }
                                .then(if (isSelected) Modifier.border(3.dp, Primary, CardShape) else Modifier),
                            shape = CardShape,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            AsyncImage(
                                model = cloth.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DraggableOutfitCanvas(
    selectedClothes: List<Cloth>,
    clothItemsWithPosition: Map<Long, com.woowa.weatherfit.domain.model.CodyClothItem>,
    selectedClothId: Long?,
    onSelectCloth: (Long) -> Unit,
    onUpdatePosition: (Long, Double, Double, Double) -> Unit,
    onUpdateZIndex: (Long, Int) -> Unit,
    onRemoveCloth: (Cloth) -> Unit,
    onViewReady: (View) -> Unit,
    modifier: Modifier = Modifier
) {
    var canvasSize by remember { mutableStateOf(IntOffset.Zero) }

    AndroidView(
        factory = { context ->
            ComposeView(context).apply {
                setContent {
                    CanvasContent(
                        selectedClothes = selectedClothes,
                        clothItemsWithPosition = clothItemsWithPosition,
                        selectedClothId = selectedClothId,
                        canvasSize = canvasSize,
                        onSelectCloth = onSelectCloth,
                        onUpdatePosition = onUpdatePosition,
                        onUpdateZIndex = onUpdateZIndex,
                        onRemoveCloth = onRemoveCloth,
                        onSizeChanged = { size -> canvasSize = size }
                    )
                }
                onViewReady(this)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun CanvasContent(
    selectedClothes: List<Cloth>,
    clothItemsWithPosition: Map<Long, com.woowa.weatherfit.domain.model.CodyClothItem>,
    selectedClothId: Long?,
    canvasSize: IntOffset,
    onSelectCloth: (Long) -> Unit,
    onUpdatePosition: (Long, Double, Double, Double) -> Unit,
    onUpdateZIndex: (Long, Int) -> Unit,
    onRemoveCloth: (Cloth) -> Unit,
    onSizeChanged: (IntOffset) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .onSizeChanged { size ->
                onSizeChanged(IntOffset(size.width, size.height))
            }
    ) {
        if (selectedClothes.isEmpty()) {
            Text(
                text = "옷을 선택하면 여기에 배치됩니다.\n드래그로 위치를 조정하세요.",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            // Draw clothes sorted by z-index
            selectedClothes
                .mapNotNull { cloth ->
                    clothItemsWithPosition[cloth.id]?.let { item -> cloth to item }
                }
                .sortedBy { (_, item) -> item.zIndex }
                .forEach { (cloth, item) ->
                    DraggableClothItem(
                        cloth = cloth,
                        position = item,
                        isSelected = cloth.id == selectedClothId,
                        canvasSize = canvasSize,
                        onSelect = { onSelectCloth(cloth.id) },
                        onUpdatePosition = { newX, newY ->
                            onUpdatePosition(cloth.id, newX, newY, item.scale)
                        },
                        onUpdateScale = { newScale ->
                            onUpdatePosition(cloth.id, item.xCoord, item.yCoord, newScale)
                        },
                        onUpdateZIndex = { newZIndex ->
                            onUpdateZIndex(cloth.id, newZIndex)
                        },
                        onRemove = { onRemoveCloth(cloth) }
                    )
                }
        }
    }
}

@Composable
fun DraggableClothItem(
    cloth: Cloth,
    position: com.woowa.weatherfit.domain.model.CodyClothItem,
    isSelected: Boolean,
    canvasSize: IntOffset,
    onSelect: () -> Unit,
    onUpdatePosition: (Double, Double) -> Unit,
    onUpdateScale: (Double) -> Unit,
    onUpdateZIndex: (Int) -> Unit,
    onRemove: () -> Unit
) {
    var offsetX by remember(position.xCoord) { mutableStateOf((position.xCoord * canvasSize.x).toFloat()) }
    var offsetY by remember(position.yCoord) { mutableStateOf((position.yCoord * canvasSize.y).toFloat()) }

    val clothSize = (80 * position.scale).dp

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    (offsetX - clothSize.toPx() / 2).roundToInt(),
                    (offsetY - clothSize.toPx() / 2).roundToInt()
                )
            }
            .size(clothSize)
    ) {
        // Cloth Image
        AsyncImage(
            model = cloth.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = if (isSelected) 3.dp else 2.dp,
                    color = if (isSelected) Color.Blue else Primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onSelect() }
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount.x).coerceIn(0f, canvasSize.x.toFloat())
                        offsetY = (offsetY + dragAmount.y).coerceIn(0f, canvasSize.y.toFloat())

                        val normalizedX = (offsetX / canvasSize.x).toDouble().coerceIn(0.0, 1.0)
                        val normalizedY = (offsetY / canvasSize.y).toDouble().coerceIn(0.0, 1.0)
                        onUpdatePosition(normalizedX, normalizedY)
                    }
                },
            contentScale = ContentScale.Crop
        )

        // Controls when selected
        if (isSelected) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Layer controls
                Row(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Move backward
                    IconButton(
                        onClick = {
                            val newZIndex = position.zIndex - 1
                            onUpdateZIndex(newZIndex)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, "뒤로 보내기", tint = Primary)
                    }

                    // Move forward
                    IconButton(
                        onClick = {
                            val newZIndex = position.zIndex + 1
                            onUpdateZIndex(newZIndex)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, "앞으로 가져오기", tint = Primary)
                    }
                }

                // Scale controls
                Row(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Decrease size
                    IconButton(
                        onClick = {
                            val newScale = (position.scale - 0.2).coerceAtLeast(0.5)
                            onUpdateScale(newScale)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Remove, "크기 줄이기", tint = Primary)
                    }

                    // Remove cloth
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Close, "삭제", tint = Color.Red)
                    }

                    // Increase size
                    IconButton(
                        onClick = {
                            val newScale = (position.scale + 0.2).coerceAtMost(2.0)
                            onUpdateScale(newScale)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, "크기 키우기", tint = Primary)
                    }
                }
            }
        }
    }
}

private fun captureViewToBitmap(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

private suspend fun saveBitmapToFile(context: Context, bitmap: Bitmap): File = withContext(Dispatchers.IO) {
    val file = File(context.cacheDir, "cody_thumbnail_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { out: FileOutputStream ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    file
}
