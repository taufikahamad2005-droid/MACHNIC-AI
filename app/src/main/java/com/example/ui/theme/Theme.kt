package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBluePrimary,
    onPrimary = TextOnBlue,
    primaryContainer = NavyCardElevated,
    onPrimaryContainer = ElectricBlueLight,
    secondary = ElectricBlueLight,
    onSecondary = NavyDarkBackground,
    secondaryContainer = NavyCardBackground,
    onSecondaryContainer = TextPrimary,
    tertiary = DiagnosticGreen,
    onTertiary = NavyDarkBackground,
    background = NavyDarkBackground,
    onBackground = TextPrimary,
    surface = NavyDarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = NavyCardBackground,
    onSurfaceVariant = TextSecondary,
    outline = NavyBorder,
    outlineVariant = NavyCardElevated,
    error = DiagnosticRed,
    onError = TextOnBlue
)

@Composable
fun MechanicAITheme(
    darkTheme: Boolean = true, // Automotive diagnostics is optimized for dark high-contrast mode
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = NavyDarkBackground.toArgb()
                window.navigationBarColor = NavyDarkBackground.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
