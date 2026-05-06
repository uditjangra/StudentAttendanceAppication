package com.udit.studentattendanceappication.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.model.UserRole

/**
 * AttendanceApp — the root composable of the entire app.
 *
 * This function decides which screen to show based on the current state:
 *   1. Splash screen (shown for 2.5 seconds on first launch)
 *   2. Login screen (shown when no user is logged in)
 *   3. Attendance marking screen (shown when teacher opens a class)
 *   4. Teacher dashboard or Student dashboard (based on role)
 *
 * viewModel() gives us the AttendanceViewModel which holds all the app state.
 */
@Composable
fun AttendanceApp(viewModel: AttendanceViewModel = viewModel()) {

    // Read the current UI state from the ViewModel
    val state = viewModel.uiState

    // Step 1: Show splash screen on first launch
    // showSplash starts as true and becomes false after 2.5 seconds
    if (viewModel.showSplash) {
        SplashScreen(onFinished = viewModel::splashFinished)
        return
    }

    // Step 2: Show login screen if no user is logged in
    if (state.loginResult == null) {
        LoginScreen(
            errorMessage = state.errorMessage,
            onLogin = viewModel::login
        )
        return
    }

    val loginResult = state.loginResult
    val isTeacher = loginResult.role == UserRole.Teacher

    // Step 3: Show attendance marking screen if teacher opened a class
    if (state.selectedClassId != null) {
        val classInfo = AttendanceRepository.schedule.first { it.id == state.selectedClassId }
        val date = state.selectedDate
        val sectionStudents = AttendanceRepository.studentsInSection(classInfo.classSection)

        AttendanceScreen(
            classInfo = classInfo,
            date = date,
            sectionStudents = sectionStudents,
            attendance = viewModel.attendanceFor(classInfo.id, date),
            presentCount = viewModel.presentCount(classInfo.id, date),
            absentCount = viewModel.absentCount(classInfo.id, date),
            onToggleAttendance = { studentId, present ->
                viewModel.markAttendance(classInfo.id, studentId, present, date)
            },
            onMarkAllPresent = { viewModel.markAllPresent(classInfo.id, date) },
            onShowSnackbar = viewModel::showSnackbar,
            onBack = viewModel::closeAttendance
        )
        return
    }

    // Step 4: Filter the schedule to only show classes for the selected day
    // Teachers see only their own classes; students see only their section's classes
    val selectedDayClasses = AttendanceRepository.schedule.filter { cls ->
        cls.dayOfWeek == state.selectedDate.dayOfWeek && when {
            isTeacher -> cls.teacherId == loginResult.userId
            else      -> cls.classSection == loginResult.classSection
        }
    }.sortedBy { it.periodNumber }   // sort by period number (1st, 2nd, 3rd...)

    val weekDates = datesForWeek(state.selectedDate)

    // Step 5: Show the correct dashboard based on role
    if (isTeacher) {
        TeacherDashboardScreen(
            uiState = state,
            classes = selectedDayClasses,
            weekDates = weekDates,
            onSelectTab = viewModel::selectTeacherTab,
            onLogout = viewModel::logout,
            onChangeMonth = viewModel::changeMonth,
            onSelectDate = viewModel::selectDate,
            onTakeAttendance = viewModel::openAttendance,
            onShowSnackbar = viewModel::showSnackbar
        )
    } else {
        StudentDashboardScreen(
            uiState = state,
            classes = selectedDayClasses,
            weekDates = weekDates,
            onSelectTab = viewModel::selectStudentTab,
            onLogout = viewModel::logout,
            onChangeMonth = viewModel::changeMonth,
            onSelectDate = viewModel::selectDate,
            onShowSnackbar = viewModel::showSnackbar
        )
    }
}
