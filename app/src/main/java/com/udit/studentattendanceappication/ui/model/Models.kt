package com.udit.studentattendanceappication.ui.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

// Student data class — holds all student information
data class Student(
    val serialNumber: Int,
    val id: String,
    val name: String,
    val classSection: String,
    val rollNumber: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val bloodGroup: String = "",
    val address: String = "",
    val parentName: String = "",
    val parentPhone: String = "",
    val parentOccupation: String = "",
    val sportsHouse: String = "",
    val sports: String = "",
    val busRoute: String = "",
    val admissionYear: String = "2022"
)

// Teacher data class — holds all teacher information
data class Teacher(
    val id: String,
    val name: String,
    val subject: String,
    val age: Int = 0,
    val qualification: String = "",
    val experience: String = "",
    val phone: String = "",
    val email: String = "",
    val classTeacherOf: String = "",
    val subjectsTaught: List<String> = emptyList(),
    val joinYear: String = ""
)

// ClassSchedule — one period in the school timetable
data class ClassSchedule(
    val id: String,
    val subject: String,
    val classSection: String,
    val dayOfWeek: DayOfWeek,
    val startTime: String,
    val endTime: String,
    val teacherId: String,
    val room: String,
    val periodNumber: Int
)

// LoginResult — stored after a successful login
data class LoginResult(
    val role: UserRole,
    val userId: String,
    val displayName: String,
    val classSection: String = ""
)

// UserRole — either Teacher or Student
enum class UserRole { Teacher, Student }

// DashboardTab — the 4 tabs in the student bottom navigation bar
enum class DashboardTab(val label: String) {
    Home("Home"),
    MyPlan("My Plan"),
    Attendance("Attendance"),
    Profile("Profile")
}

// TeacherTab — the 3 tabs in the teacher bottom navigation bar
enum class TeacherTab(val label: String) {
    Home("Home"),
    Calendar("Calendar"),
    Profile("Profile")
}

// FreePeriod — a gap between classes (e.g. lunch break)
data class FreePeriod(
    val startTime: String,
    val endTime: String,
    val dayOfWeek: DayOfWeek
)

// ActivitySuggestion — a study/wellness activity shown during free periods
data class ActivitySuggestion(
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val category: String,   // e.g. "Study", "Revision", "Wellness"
    val categoryColor: Long // color as a hex Long, e.g. 0xFF3C8D84
)

// AppUiState — the single source of truth for the entire app's UI state
// The ViewModel holds one instance of this and updates it on every action
data class AppUiState(
    val loginResult: LoginResult? = null,       // null = not logged in
    val selectedStudentTab: DashboardTab = DashboardTab.Home,
    val selectedTeacherTab: TeacherTab = TeacherTab.Home,
    val selectedMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedClassId: String? = null,        // null = attendance screen not open
    val errorMessage: String? = null,
    val snackbarMessage: String? = null
)
