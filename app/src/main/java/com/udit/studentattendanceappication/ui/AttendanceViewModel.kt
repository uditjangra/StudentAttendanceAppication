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

class AttendanceViewModel : ViewModel() {

    var uiState by mutableStateOf(AppUiState())
        private set

    // Key: "classId|date" -> Map<studentId, Boolean>
    private val attendanceState = mutableStateMapOf<String, MutableMap<String, Boolean>>()

    // ─── Auth ────────────────────────────────────────────────────────────────

    fun login(userId: String, password: String) {
        if (password != "123") {
            uiState = uiState.copy(errorMessage = "Invalid password. Use 123.")
            return
        }

        val teacher = AttendanceRepository.teachers.firstOrNull { it.id == userId }
        if (teacher != null) {
            uiState = AppUiState(
                loginResult = LoginResult(UserRole.Teacher, teacher.id, teacher.name),
                selectedDate = LocalDate.now(),
                selectedMonth = YearMonth.now()
            )
            return
        }

        val student = AttendanceRepository.students.firstOrNull { it.id == userId }
        if (student != null) {
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

        uiState = uiState.copy(errorMessage = "User ID not found.")
    }

    fun logout() {
        uiState = AppUiState()
    }

    // ─── Navigation ──────────────────────────────────────────────────────────

    fun selectStudentTab(tab: DashboardTab) {
        uiState = uiState.copy(selectedStudentTab = tab)
    }

    fun selectTeacherTab(tab: TeacherTab) {
        uiState = uiState.copy(selectedTeacherTab = tab)
    }

    fun openAttendance(classId: String) {
        uiState = uiState.copy(selectedClassId = classId)
    }

    fun closeAttendance() {
        uiState = uiState.copy(selectedClassId = null)
    }

    // ─── Calendar ────────────────────────────────────────────────────────────

    fun changeMonth(offset: Long) {
        val nextMonth = uiState.selectedMonth.plusMonths(offset)
        val adjustedDay = minOf(uiState.selectedDate.dayOfMonth, nextMonth.lengthOfMonth())
        uiState = uiState.copy(
            selectedMonth = nextMonth,
            selectedDate = LocalDate.of(nextMonth.year, nextMonth.month, adjustedDay)
        )
    }

    fun selectDate(date: LocalDate) {
        uiState = uiState.copy(
            selectedDate = date,
            selectedMonth = YearMonth.from(date)
        )
    }

    // ─── Attendance ──────────────────────────────────────────────────────────

    private fun attendanceKey(classId: String, date: LocalDate) = "$classId|$date"

    fun markAttendance(classId: String, studentId: String, present: Boolean, date: LocalDate) {
        val key = attendanceKey(classId, date)
        val section = AttendanceRepository.schedule.firstOrNull { it.id == classId }?.classSection ?: ""
        val classAttendance = attendanceState.getOrPut(key) {
            AttendanceRepository.studentsInSection(section).associate { it.id to false }.toMutableMap()
        }
        classAttendance[studentId] = present
        attendanceState[key] = classAttendance
    }

    fun markAllPresent(classId: String, date: LocalDate) {
        val key = attendanceKey(classId, date)
        val section = AttendanceRepository.schedule.firstOrNull { it.id == classId }?.classSection ?: ""
        val classAttendance = attendanceState.getOrPut(key) {
            AttendanceRepository.studentsInSection(section).associate { it.id to false }.toMutableMap()
        }
        AttendanceRepository.studentsInSection(section).forEach { classAttendance[it.id] = true }
        attendanceState[key] = classAttendance
    }

    fun attendanceFor(classId: String, date: LocalDate): Map<String, Boolean> {
        val key = attendanceKey(classId, date)
        val section = AttendanceRepository.schedule.firstOrNull { it.id == classId }?.classSection ?: ""
        return attendanceState.getOrPut(key) {
            AttendanceRepository.studentsInSection(section).associate { it.id to false }.toMutableMap()
        }
    }

    fun presentCount(classId: String, date: LocalDate): Int =
        attendanceFor(classId, date).values.count { it }

    fun absentCount(classId: String, date: LocalDate): Int =
        attendanceFor(classId, date).values.count { !it }
}
