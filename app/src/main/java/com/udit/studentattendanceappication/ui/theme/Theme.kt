package com.udit.studentattendanceappication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TealSecondary,
    secondary = AccentAmber,
    tertiary = AccentAmber,
    background = Color(0xFF122225),
    surface = Color(0xFF193034),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFF1F6F5),
    onSurface = Color(0xFFF1F6F5)
)

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    secondary = TealSecondary,
    tertiary = AccentAmber,
    background = ScreenBackground,
    surface = WarmSurface,
    onPrimary = Color.White,
    onSecondary = Color(0xFF143236),
    onTertiary = Color.White,
    onBackground = Color(0xFF172B2E),
    onSurface = Color(0xFF172B2E)
)

@Composable
fun StudentAttendanceAppicationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
