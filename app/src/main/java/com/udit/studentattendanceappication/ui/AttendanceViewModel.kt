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
import com.udit.studentattendanceappication.ui.model.UserRole
import java.time.LocalDate
import java.time.YearMonth

class AttendanceViewModel : ViewModel() {
    var uiState by mutableStateOf(AppUiState())
        private set

    private val attendanceState = mutableStateMapOf<String, MutableMap<String, Boolean>>()

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
                loginResult = LoginResult(UserRole.Student, student.id, student.name),
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

    fun selectTab(tab: DashboardTab) {
        uiState = uiState.copy(selectedTab = tab)
    }

    fun openAttendance(classId: String) {
        uiState = uiState.copy(selectedClassId = classId)
    }

    fun closeAttendance() {
        uiState = uiState.copy(selectedClassId = null)
    }

    fun changeMonth(offset: Long) {
        val nextMonth = uiState.selectedMonth.plusMonths(offset)
        val currentSelected = uiState.selectedDate
        val adjustedDay = minOf(currentSelected.dayOfMonth, nextMonth.lengthOfMonth())
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

    fun markAttendance(classId: String, studentId: String, present: Boolean) {
        val classAttendance = attendanceState.getOrPut(classId) {
            AttendanceRepository.students.associate { it.id to false }.toMutableMap()
        }
        classAttendance[studentId] = present
        attendanceState[classId] = classAttendance
    }

    fun attendanceFor(classId: String): Map<String, Boolean> {
        return attendanceState.getOrPut(classId) {
            AttendanceRepository.students.associate { it.id to false }.toMutableMap()
        }
    }
}
