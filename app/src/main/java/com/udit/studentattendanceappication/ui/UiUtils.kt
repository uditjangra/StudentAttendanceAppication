package com.udit.studentattendanceappication.ui

import java.time.LocalDate

fun datesForWeek(anchorDate: LocalDate): List<LocalDate> {
    val start = anchorDate.minusDays((anchorDate.dayOfWeek.value - 1).toLong())
    return List(7) { start.plusDays(it.toLong()) }
}
