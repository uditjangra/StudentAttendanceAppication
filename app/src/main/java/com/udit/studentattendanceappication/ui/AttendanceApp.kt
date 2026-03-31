package com.udit.studentattendanceappication.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.model.DashboardTab
import com.udit.studentattendanceappication.ui.model.UserRole

@Composable
fun AttendanceApp(viewModel: AttendanceViewModel = viewModel()) {
    val state = viewModel.uiState

    if (state.loginResult == null) {
        LoginScreen(
            errorMessage = state.errorMessage,
            onLogin = viewModel::login
        )
        return
    }

    if (state.selectedClassId != null) {
        val classInfo = AttendanceRepository.schedule.first { it.id == state.selectedClassId }
        AttendanceScreen(
            classInfo = classInfo,
            attendance = viewModel.attendanceFor(classInfo.id),
            onToggleAttendance = { studentId, present ->
                viewModel.markAttendance(classInfo.id, studentId, present)
            },
            onBack = viewModel::closeAttendance
        )
        return
    }

    val loginResult = state.loginResult
    val isTeacher = loginResult.role == UserRole.Teacher
    val selectedDayClasses = AttendanceRepository.schedule.filter {
        it.dayOfWeek == state.selectedDate.dayOfWeek &&
            (!isTeacher || it.teacherId == loginResult.userId)
    }

    DashboardScreen(
        uiState = state,
        classes = selectedDayClasses,
        weekDates = datesForWeek(state.selectedDate),
        onSelectTab = viewModel::selectTab,
        onLogout = viewModel::logout,
        onChangeMonth = viewModel::changeMonth,
        onSelectDate = viewModel::selectDate,
        onTakeAttendance = viewModel::openAttendance
    )
}
