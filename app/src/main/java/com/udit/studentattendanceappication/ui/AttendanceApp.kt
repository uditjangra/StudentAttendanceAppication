package com.udit.studentattendanceappication.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.model.UserRole

@Composable
fun AttendanceApp(viewModel: AttendanceViewModel = viewModel()) {
    val state = viewModel.uiState

    if (state.loginResult == null) {
        LoginScreen(errorMessage = state.errorMessage, onLogin = viewModel::login)
        return
    }

    val loginResult = state.loginResult ?: return
    val isTeacher = loginResult.role == UserRole.Teacher

    if (state.selectedClassId != null) {
        val classInfo = AttendanceRepository.schedule.first { it.id == state.selectedClassId }
        val date = state.selectedDate
        val sectionStudents = AttendanceRepository.studentsInSection(classInfo.classSection)
        AttendanceScreen(
            classInfo = classInfo, date = date, sectionStudents = sectionStudents,
            attendance = viewModel.attendanceFor(classInfo.id, date),
            presentCount = viewModel.presentCount(classInfo.id, date),
            absentCount = viewModel.absentCount(classInfo.id, date),
            onToggleAttendance = { studentId, present -> viewModel.markAttendance(classInfo.id, studentId, present, date) },
            onMarkAllPresent = { viewModel.markAllPresent(classInfo.id, date) },
            onBack = viewModel::closeAttendance
        )
        return
    }

    val selectedDayClasses = AttendanceRepository.schedule.filter { cls ->
        cls.dayOfWeek == state.selectedDate.dayOfWeek && when {
            isTeacher -> cls.teacherId == loginResult.userId
            else      -> cls.classSection == loginResult.classSection
        }
    }.sortedBy { it.periodNumber }

    val weekDates = datesForWeek(state.selectedDate)

    if (isTeacher) {
        TeacherDashboardScreen(
            uiState = state, classes = selectedDayClasses, weekDates = weekDates,
            onSelectTab = viewModel::selectTeacherTab, onLogout = viewModel::logout,
            onChangeMonth = viewModel::changeMonth, onSelectDate = viewModel::selectDate,
            onTakeAttendance = viewModel::openAttendance,
            onShowSnackbar = viewModel::showSnackbar
        )
    } else {
        StudentDashboardScreen(
            uiState = state, classes = selectedDayClasses, weekDates = weekDates,
            onSelectTab = viewModel::selectStudentTab, onLogout = viewModel::logout,
            onChangeMonth = viewModel::changeMonth, onSelectDate = viewModel::selectDate,
            onShowSnackbar = viewModel::showSnackbar
        )
    }
}
