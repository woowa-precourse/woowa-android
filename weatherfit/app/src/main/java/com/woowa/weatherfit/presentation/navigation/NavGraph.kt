package com.woowa.weatherfit.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.woowa.weatherfit.presentation.common.WeatherFitBottomBar
import com.woowa.weatherfit.presentation.screen.cloth.AddClothScreen
import com.woowa.weatherfit.presentation.screen.cloth.ClothDetailScreen
import com.woowa.weatherfit.presentation.screen.cloth.ClothListScreen
import com.woowa.weatherfit.presentation.screen.cody.CodyDetailScreen
import com.woowa.weatherfit.presentation.screen.cody.CodyEditScreen
import com.woowa.weatherfit.presentation.screen.cody.CodyListScreen
import com.woowa.weatherfit.presentation.screen.home.HomeScreen
import com.woowa.weatherfit.presentation.screen.region.RegionSelectScreen

@Composable
fun WeatherFitNavHost(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        Routes.Home.route,
        Routes.ClothList.route,
        Routes.CodyList.route
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                WeatherFitBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Routes.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Routes.Home.route) {
                HomeScreen(
                    navController = navController,
                    onNavigateToRegionSelect = {
                        navController.navigate(Routes.RegionSelect.route)
                    },
                    onNavigateToCodyDetail = { codyId ->
                        navController.navigate(Routes.CodyDetail.createRoute(codyId))
                    }
                )
            }

            composable(Routes.ClothList.route) {
                ClothListScreen(
                    onNavigateToAddCloth = { clothId ->
                        navController.navigate(Routes.AddCloth.createRoute(clothId))
                    },
                    onNavigateToClothDetail = { clothId ->
                        navController.navigate(Routes.ClothDetail.createRoute(clothId))
                    }
                )
            }

            composable(Routes.CodyList.route) {
                CodyListScreen(
                    onNavigateToCodyEdit = {
                        navController.navigate(Routes.CodyEdit.createRoute())
                    },
                    onNavigateToCodyDetail = { codyId ->
                        navController.navigate(Routes.CodyDetail.createRoute(codyId))
                    }
                )
            }

            composable(
                route = Routes.AddCloth.route,
                arguments = listOf(
                    navArgument("clothId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) {
                val clothId = it.arguments?.getLong("clothId")?.takeIf { it != -1L }
                AddClothScreen(
                    clothId = clothId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.ClothDetail.route,
                arguments = listOf(navArgument("clothId") { type = NavType.LongType })
            ) {
                val clothId = it.arguments?.getLong("clothId") ?: 0L
                ClothDetailScreen(
                    clothId = clothId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.CodyEdit.route,
                arguments = listOf(
                    navArgument("codyId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) {
                CodyEditScreen(
                    navController = navController,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.CodyDetail.route,
                arguments = listOf(navArgument("codyId") { type = NavType.LongType })
            ) {
                CodyDetailScreen(
                    navController = navController,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCodyDetail = { codyId ->
                        navController.navigate(Routes.CodyDetail.createRoute(codyId))
                    },
                    onNavigateToEdit = { codyId ->
                        navController.navigate(Routes.CodyEdit.createRoute(codyId))
                    }
                )
            }

            composable(Routes.RegionSelect.route) {
                RegionSelectScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
