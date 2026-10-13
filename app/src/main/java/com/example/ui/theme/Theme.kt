package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SmileStudioColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Color(0xFF141208),
    primaryContainer = GoldContainer,
    onPrimaryContainer = OnGoldContainer,
    secondary = GoldLight,
    onSecondary = Color(0xFF141208),
    secondaryContainer = Color(0xFF2E2614),
    onSecondaryContainer = Color(0xFFFFECC4),
    tertiary = Color(0xFFE0C477),
    onTertiary = Color(0xFF1B1607),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder,
    outlineVariant = DarkBorderGold,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SmileStudioColorScheme,
        typography = Typography,
        content = content
    )
}
