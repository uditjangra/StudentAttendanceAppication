package com.udit.studentattendanceappication.ui.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

data class Student(
    val serialNumber: Int,
    val id: String,
    val name: String,
    val classSection: String   // e.g. "10-A"
)

data class Teacher(
    val id: String,
    val name: String,
    val subject: String        // primary subject they teach
)

data class ClassSchedule(
    val id: String,
    val subject: String,
    val classSection: String,  // e.g. "10-A", "10-B"
    val dayOfWeek: DayOfWeek,
    val startTime: String,
    val endTime: String,
    val teacherId: String,
    val room: String,
    val periodNumber: Int      // 1st period, 2nd period, etc.
)

data class LoginResult(
    val role: UserRole,
    val userId: String,
    val displayName: String,
    val classSection: String = ""
)

enum class UserRole { Teacher, Student }

enum class StudentGoal(val label: String, val emoji: String, val description: String) {
    Engineering("Engineering",      "⚙️",  "Focus on Math, Physics & CS"),
    Medicine   ("Medicine",         "🩺",  "Focus on Biology, Chemistry & Physics"),
    Arts       ("Arts & Humanities","🎨",  "Focus on English, History & Literature"),
    Commerce   ("Commerce",         "📊",  "Focus on Math, Economics & Accounts"),
    Research   ("Research & Science","🔬", "Focus on all sciences & analytical skills")
}

enum class DashboardTab(val label: String) {
    Home("Home"), MyPlan("My Plan"), Attendance("Attendance"), Profile("Profile")
}

enum class TeacherTab(val label: String) {
    Home("Home"), Calendar("Calendar"), Profile("Profile")
}

typealias DateAwareAttendance = Map<String, Map<String, Boolean>>

data class FreePeriod(
    val startTime: String,
    val endTime: String,
    val dayOfWeek: DayOfWeek
)

data class ActivitySuggestion(
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val category: ActivityCategory,
    val goals: List<StudentGoal>
)

enum class ActivityCategory(val label: String, val color: Long) {
    Study   ("Study",    0xFF3C8D84),
    Revision("Revision", 0xFFF2A65A),
    Reading ("Reading",  0xFF7B68EE),
    Practice("Practice", 0xFF4CAF7A),
    Wellness("Wellness", 0xFFE66B6B),
    Creative("Creative", 0xFFFF8C69)
}

data class AppUiState(
    val loginResult: LoginResult? = null,
    val selectedStudentTab: DashboardTab = DashboardTab.Home,
    val selectedTeacherTab: TeacherTab = TeacherTab.Home,
    val selectedMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedClassId: String? = null,
    val errorMessage: String? = null
)
