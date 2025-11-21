package com.woowa.weatherfit.presentation.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.woowa.weatherfit.R
import com.woowa.weatherfit.presentation.navigation.BottomNavItem
import com.woowa.weatherfit.ui.theme.BottomNavBackground
import com.woowa.weatherfit.ui.theme.BottomNavSelected
import com.woowa.weatherfit.ui.theme.BottomNavUnselected

@Composable
fun WeatherFitBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = BottomNavBackground
    ) {
        BottomNavItem.entries.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        painter = getIconForItem(item, isSelected),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BottomNavSelected,
                    selectedTextColor = BottomNavSelected,
                    unselectedIconColor = BottomNavUnselected,
                    unselectedTextColor = BottomNavUnselected,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun getIconForItem(item: BottomNavItem, isSelected: Boolean): Painter {
    return when (item) {
        BottomNavItem.HOME ->
            if (isSelected) painterResource(R.drawable.home_1)
            else painterResource(R.drawable.home)

        BottomNavItem.CLOTH ->
            if (isSelected) painterResource(R.drawable.clothes_1)
            else painterResource(R.drawable.clothes)

        BottomNavItem.CODY ->
            if (isSelected) painterResource(R.drawable.cody_1)
            else painterResource(R.drawable.cody)
    }
}

