package com.udit.studentattendanceappication.ui.theme

import androidx.compose.ui.graphics.Color

// ── Schoolastica brand ────────────────────────────────────────────────────────
val SchoolGreen        = Color(0xFF1DB87E)   // primary sidebar / login bg
val SchoolGreenDark    = Color(0xFF179966)   // pressed / active states
val SchoolGreenLight   = Color(0xFF4ECFA0)   // lighter tint for badges
val SidebarBg          = Color(0xFF18A870)   // sidebar background
val ActiveNavPill      = Color(0xFFFFFFFF)   // white pill behind active nav item
val ActiveNavText      = Color(0xFF18A870)   // green text on white pill

// ── Content area ─────────────────────────────────────────────────────────────
val ContentBg          = Color(0xFFF5F0E8)   // cream/off-white main area
val CardWhite          = Color(0xFFFFFFFF)
val DividerColor       = Color(0xFFEAE6DE)

// ── Text ─────────────────────────────────────────────────────────────────────
val TextPrimary        = Color(0xFF1A1A2E)
val TextSecondary      = Color(0xFF6B7280)
val TextMuted          = Color(0xFF9CA3AF)
val TextOnGreen        = Color(0xFFFFFFFF)

// ── Subject / class card palette (Schoolastica colorful cards) ────────────────
val CardTeal           = Color(0xFF2A9D8F)
val CardBlue           = Color(0xFF3B5BDB)
val CardRed            = Color(0xFFE63946)
val CardBrown          = Color(0xFF6D4C41)
val CardPurple         = Color(0xFF7B2FBE)
val CardGreen          = Color(0xFF2D9E5F)
val CardOrange         = Color(0xFFE87722)
val CardIndigo         = Color(0xFF4361EE)

val SubjectColors = listOf(CardTeal, CardBlue, CardRed, CardBrown, CardPurple, CardGreen, CardOrange, CardIndigo)

// ── Status ────────────────────────────────────────────────────────────────────
val AttendancePresent  = Color(0xFF2ECC71)
val AttendanceAbsent   = Color(0xFFE74C3C)
val AttendanceLate     = Color(0xFFF39C12)

// ── Legacy aliases (keep existing references compiling) ───────────────────────
val TealPrimary        = SchoolGreen
val TealSecondary      = SchoolGreenLight
val AccentAmber        = Color(0xFFE87722)
val ScreenBackground   = ContentBg
val CardDark           = Color(0xFF1A3A4A)
val MutedText          = TextSecondary
val WarmSurface        = CardWhite
