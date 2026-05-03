package com.udit.studentattendanceappication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.model.AppUiState
import com.udit.studentattendanceappication.ui.model.DashboardTab
import com.udit.studentattendanceappication.ui.model.LoginResult
import com.udit.studentattendanceappication.ui.model.TeacherTab
import com.udit.studentattendanceappication.ui.model.UserRole
import java.time.LocalDate
import java.time.YearMonth

/**
 * AttendanceViewModel — the brain of the app.
 *
 * In Android's MVVM (Model-View-ViewModel) pattern:
 *  - Model = AttendanceRepository (the data)
 *  - View = all the @Composable screens
 *  - ViewModel = this class (holds state, handles user actions)
 *
 * The ViewModel survives screen rotations and config changes.
 * All UI state is stored here so the screens just read and display it.
 */
class AttendanceViewModel : ViewModel() {

    // uiState holds everything the UI needs to display.
    // mutableStateOf means: when this value changes, Compose automatically redraws the screen.
    var uiState by mutableStateOf(AppUiState())
        private set   // only this ViewModel can change it; screens can only read it

    // showSplash controls whether the splash screen is visible.
    // Starts as true, set to false after 2.5 seconds.
    var showSplash by mutableStateOf(true)
        private set

    // attendanceState stores which students are present/absent for each class on each date.
    // The key is "classId|date" (e.g. "MON-A1|2026-05-01") so each class+date combination
    // has its own separate attendance record.
    // mutableStateMapOf means: when entries change, Compose redraws screens that use this map.
    private val attendanceState = mutableStateMapOf<String, MutableMap<String, Boolean>>()

    // Called by SplashScreen after 2.5 seconds to navigate to login
    fun splashFinished() {
        showSplash = false
    }

    // ── Login / Logout ────────────────────────────────────────────────────────

    fun login(userId: String, password: String) {
        // Check password first — all users share the same demo password "123"
        if (password != "123") {
            uiState = uiState.copy(errorMessage = "Invalid password. Use 123.")
            return
        }

        // Check if the userId belongs to a teacher
        val teacher = AttendanceRepository.teachers.firstOrNull { it.id == userId }
        if (teacher != null) {
            // Login successful as teacher — create a fresh AppUiState
            uiState = AppUiState(
                loginResult = LoginResult(UserRole.Teacher, teacher.id, teacher.name),
                selectedDate = LocalDate.now(),
                selectedMonth = YearMonth.now()
            )
            return
        }

        // Check if the userId belongs to a student
        val student = AttendanceRepository.students.firstOrNull { it.id == userId }
        if (student != null) {
            // Login successful as student
            uiState = AppUiState(
                loginResult = LoginResult(
                    role = UserRole.Student,
                    userId = student.id,
                    displayName = student.name,
                    classSection = student.classSection
                ),
                selectedDate = LocalDate.now(),
                selectedMonth = YearMonth.now()
            )
            return
        }

        // Neither teacher nor student found
        uiState = uiState.copy(errorMessage = "User ID not found.")
    }

    fun logout() {
        // Reset to a blank AppUiState — this takes the user back to the login screen
        uiState = AppUiState()
    }

    // ── Tab Navigation ────────────────────────────────────────────────────────

    // Called when the student taps a bottom nav tab
    fun selectStudentTab(tab: DashboardTab) {
        uiState = uiState.copy(selectedStudentTab = tab)
    }

    // Called when the teacher taps a bottom nav tab
    fun selectTeacherTab(tab: TeacherTab) {
        uiState = uiState.copy(selectedTeacherTab = tab)
    }

    // Called when teacher taps "Mark Attendance" on a class card
    fun openAttendance(classId: String) {
        uiState = uiState.copy(selectedClassId = classId)
    }

    // Called when teacher taps the back button on the attendance screen
    fun closeAttendance() {
        uiState = uiState.copy(selectedClassId = null)
    }

    // ── Calendar Navigation ───────────────────────────────────────────────────

    // Called when teacher taps the left/right arrows on the calendar
    // offset = -1 for previous month, +1 for next month
    fun changeMonth(offset: Long) {
        val nextMonth = uiState.selectedMonth.plusMonths(offset)
        // Make sure the selected day is valid in the new month (e.g. Jan 31 -> Feb 28)
        val adjustedDay = minOf(uiState.selectedDate.dayOfMonth, nextMonth.lengthOfMonth())
        uiState = uiState.copy(
            selectedMonth = nextMonth,
            selectedDate = LocalDate.of(nextMonth.year, nextMonth.month, adjustedDay)
        )
    }

    // Called when teacher taps a date on the calendar
    fun selectDate(date: LocalDate) {
        uiState = uiState.copy(
            selectedDate = date,
            selectedMonth = YearMonth.from(date)
        )
    }

    // ── Attendance Marking ────────────────────────────────────────────────────

    // Creates a unique key for each class+date combination
    // Example: "MON-A1|2026-05-01"
    private fun attendanceKey(classId: String, date: LocalDate) = "$classId|$date"

    // Mark one student as present or absent
    fun markAttendance(classId: String, studentId: String, present: Boolean, date: LocalDate) {
        val key = attendanceKey(classId, date)
        val section = AttendanceRepository.schedule.firstOrNull { it.id == classId }?.classSection ?: ""

        // Get existing attendance map for this class+date, or create a new one with all absent
        val map = attendanceState.getOrPut(key) {
            AttendanceRepository.studentsInSection(section).associate { it.id to false }.toMutableMap()
        }
        map[studentId] = present
        attendanceState[key] = map
    }

    // Mark ALL students in the class as present at once
    fun markAllPresent(classId: String, date: LocalDate) {
        val key = attendanceKey(classId, date)
        val section = AttendanceRepository.schedule.firstOrNull { it.id == classId }?.classSection ?: ""
        val map = attendanceState.getOrPut(key) {
            AttendanceRepository.studentsInSection(section).associate { it.id to false }.toMutableMap()
        }
        // Set every student to present (true)
        AttendanceRepository.studentsInSection(section).forEach { map[it.id] = true }
        attendanceState[key] = map
        showSnackbar("All students marked present")
    }

    // Get the attendance map for a specific class on a specific date
    fun attendanceFor(classId: String, date: LocalDate): Map<String, Boolean> {
        val key = attendanceKey(classId, date)
        val section = AttendanceRepository.schedule.firstOrNull { it.id == classId }?.classSection ?: ""
        return attendanceState.getOrPut(key) {
            AttendanceRepository.studentsInSection(section).associate { it.id to false }.toMutableMap()
        }
    }

    // Count how many students are present
    fun presentCount(classId: String, date: LocalDate): Int =
        attendanceFor(classId, date).values.count { it == true }

    // Count how many students are absent
    fun absentCount(classId: String, date: LocalDate): Int =
        attendanceFor(classId, date).values.count { it == false }

    // ── Snackbar (toast-like messages) ────────────────────────────────────────

    // Show a short message at the bottom of the screen
    fun showSnackbar(message: String) {
        uiState = uiState.copy(snackbarMessage = message)
    }

    // Clear the message after it has been shown
    fun dismissSnackbar() {
        uiState = uiState.copy(snackbarMessage = null)
    }
}
