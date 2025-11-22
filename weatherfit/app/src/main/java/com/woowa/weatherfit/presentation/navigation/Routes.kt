package com.woowa.weatherfit.presentation.navigation

sealed class Routes(val route: String) {
    // Bottom Navigation Routes
    data object Home : Routes("home")
    data object ClothList : Routes("cloth_list")
    data object CodyList : Routes("cody_list")

    // Standalone Routes (no bottom bar)
    data object AddCloth : Routes("add_cloth?clothId={clothId}") {
        fun createRoute(clothId: Long? = null) = if (clothId != null) {
            "add_cloth?clothId=$clothId"
        } else {
            "add_cloth"
        }
    }
    data object ClothDetail : Routes("cloth_detail/{clothId}") {
        fun createRoute(clothId: Long) = "cloth_detail/$clothId"
    }
    data object CodyEdit : Routes("cody_edit")
    data object CodyDetail : Routes("cody_detail/{codyId}") {
        fun createRoute(codyId: Long) = "cody_detail/$codyId"
    }
    data object RegionSelect : Routes("region_select")
}

enum class BottomNavItem(
    val route: String,
    val title: String,
    val iconName: String
) {
    HOME(Routes.Home.route, "Home", "home"),
    CLOTH(Routes.ClothList.route, "Cloth", "checkroom"),
    CODY(Routes.CodyList.route, "Cody", "styler")
}
