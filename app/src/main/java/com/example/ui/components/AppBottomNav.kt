package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.localization.AppStrings
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.ElectricBluePrimary
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDarkSurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextTertiary

@Composable
fun AppBottomNav(
    currentScreen: Screen,
    strings: AppStrings,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = NavyDarkSurface,
        modifier = modifier.testTag("app_bottom_nav")
    ) {
        // 1. Dashboard
        NavigationBarItem(
            selected = currentScreen is Screen.Dashboard,
            onClick = { onNavigate(Screen.Dashboard) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = strings.dashboard,
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text(strings.dashboard, fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricBlueLight,
                selectedTextColor = ElectricBlueLight,
                indicatorColor = NavyCardElevated,
                unselectedIconColor = TextTertiary,
                unselectedTextColor = TextTertiary
            ),
            modifier = Modifier.testTag("nav_item_dashboard")
        )

        // 2. AI Diagnosis
        NavigationBarItem(
            selected = currentScreen is Screen.Search,
            onClick = { onNavigate(Screen.Search) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = strings.diagnose,
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text(strings.diagnose, fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricBlueLight,
                selectedTextColor = ElectricBlueLight,
                indicatorColor = NavyCardElevated,
                unselectedIconColor = TextTertiary,
                unselectedTextColor = TextTertiary
            ),
            modifier = Modifier.testTag("nav_item_search")
        )

        // 3. History
        NavigationBarItem(
            selected = currentScreen is Screen.History,
            onClick = { onNavigate(Screen.History) },
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = strings.history,
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text(strings.history, fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricBlueLight,
                selectedTextColor = ElectricBlueLight,
                indicatorColor = NavyCardElevated,
                unselectedIconColor = TextTertiary,
                unselectedTextColor = TextTertiary
            ),
            modifier = Modifier.testTag("nav_item_history")
        )

        // 4. Library
        NavigationBarItem(
            selected = currentScreen is Screen.Library || currentScreen is Screen.GuideDetail,
            onClick = { onNavigate(Screen.Library) },
            icon = {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = strings.library,
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text(strings.library, fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricBlueLight,
                selectedTextColor = ElectricBlueLight,
                indicatorColor = NavyCardElevated,
                unselectedIconColor = TextTertiary,
                unselectedTextColor = TextTertiary
            ),
            modifier = Modifier.testTag("nav_item_library")
        )

        // 5. Profile
        NavigationBarItem(
            selected = currentScreen is Screen.Profile || currentScreen is Screen.InstallationGuide || currentScreen is Screen.About,
            onClick = { onNavigate(Screen.Profile) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = strings.profile,
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text(strings.profile, fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricBlueLight,
                selectedTextColor = ElectricBlueLight,
                indicatorColor = NavyCardElevated,
                unselectedIconColor = TextTertiary,
                unselectedTextColor = TextTertiary
            ),
            modifier = Modifier.testTag("nav_item_profile")
        )
    }
}
