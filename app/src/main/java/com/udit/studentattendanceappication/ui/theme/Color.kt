package com.udit.studentattendanceappication.ui.theme

import androidx.compose.ui.graphics.Color

// =============================================================================
// App Color Palette
// All colors used in the app are defined here so they are easy to find and change
// =============================================================================

// Primary green — used for the login screen background, top bar, buttons, nav bar
val SchoolGreen      = Color(0xFF1DB87E)
val SchoolGreenLight = Color(0xFF4ECFA0)   // lighter green for badges

// Background colors
val ContentBg  = Color(0xFFF5F0E8)   // cream/off-white — main screen background
val CardWhite  = Color(0xFFFFFFFF)   // white — card backgrounds
val DividerColor = Color(0xFFEAE6DE) // light grey — divider lines

// Text colors
val TextPrimary   = Color(0xFF1A1A2E)   // dark — main text
val TextSecondary = Color(0xFF6B7280)   // grey — secondary text
val TextMuted     = Color(0xFF9CA3AF)   // light grey — labels, hints

// Subject card colors — each subject gets a different color card
val CardTeal   = Color(0xFF2A9D8F)
val CardBlue   = Color(0xFF3B5BDB)
val CardRed    = Color(0xFFE63946)
val CardBrown  = Color(0xFF6D4C41)
val CardPurple = Color(0xFF7B2FBE)
val CardGreen  = Color(0xFF2D9E5F)
val CardOrange = Color(0xFFE87722)
val CardIndigo = Color(0xFF4361EE)

// SubjectColors — a list used to pick a color for each subject card in order
val SubjectColors = listOf(CardTeal, CardBlue, CardRed, CardBrown, CardPurple, CardGreen, CardOrange, CardIndigo)

// Attendance status colors
val AttendancePresent = Color(0xFF2ECC71)   // green — student is present
val AttendanceAbsent  = Color(0xFFE74C3C)   // red — student is absent

// Stat card accent colors
val AccentAmber = Color(0xFFE87722)   // orange — used in stat cards
