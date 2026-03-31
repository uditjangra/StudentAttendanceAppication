package com.udit.studentattendanceappication.ui.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

data class Student(
    val serialNumber: Int,
    val id: String,
    val name: String
)

data class Teacher(
    val id: String,
    val name: String
)

data class ClassSchedule(
    val id: String,
    val subject: String,
    val dayOfWeek: DayOfWeek,
    val startTime: String,
    val endTime: String,
    val teacherId: String,
    val room: String
)

data class LoginResult(
    val role: UserRole,
    val userId: String,
    val displayName: String
)

enum class UserRole {
    Teacher,
    Student
}

enum class DashboardTab(val label: String) {
    Home("Home"),
    Calendar("Calendar"),
    Profile("Profile")
}

data class AppUiState(
    val loginResult: LoginResult? = null,
    val selectedTab: DashboardTab = DashboardTab.Home,
    val selectedMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedClassId: String? = null,
    val errorMessage: String? = null
)
