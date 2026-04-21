package com.udit.studentattendanceappication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SchoolasticaColorScheme = lightColorScheme(
    primary          = SchoolGreen,
    onPrimary        = Color.White,
    primaryContainer = SchoolGreenLight,
    onPrimaryContainer = Color.White,
    secondary        = CardBlue,
    onSecondary      = Color.White,
    tertiary         = CardOrange,
    onTertiary       = Color.White,
    background       = ContentBg,
    onBackground     = TextPrimary,
    surface          = CardWhite,
    onSurface        = TextPrimary,
    surfaceVariant   = Color(0xFFEEEBE4),
    onSurfaceVariant = TextSecondary,
    outline          = DividerColor,
    error            = AttendanceAbsent,
    onError          = Color.White
)

@Composable
fun StudentAttendanceAppicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SchoolasticaColorScheme,
        typography  = Typography,
        content     = content
    )
}
