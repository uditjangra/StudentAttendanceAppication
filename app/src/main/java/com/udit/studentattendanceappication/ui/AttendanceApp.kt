package com.udit.studentattendanceappication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.model.UserRole
import com.udit.studentattendanceappication.ui.theme.ContentBg
import com.udit.studentattendanceappication.ui.theme.SchoolGreen

/**
 * AttendanceApp — the root composable of the entire app.
 *
 * Routing order:
 *   1. Splash screen (2.5 seconds)
 *   2. Loading spinner (while Firebase login is in progress)
 *   3. Login screen (no user logged in)
 *   4. Admin dashboard (role = Admin)
 *   5. Attendance marking screen (teacher opened a class)
 *   6. Teacher dashboard or Student dashboard
 */
@Composable
fun AttendanceApp(viewModel: AttendanceViewModel = viewModel()) {
    val state = viewModel.uiState

    // Step 1: Splash screen
    if (viewModel.showSplash) {
        SplashScreen(onFinished = viewModel::splashFinished)
        return
    }

    // Step 2: Loading spinner while Firebase is working
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().background(ContentBg),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SchoolGreen)
        }
        return
    }

    // Step 3: Login screen
    if (state.loginResult == null) {
        LoginScreen(errorMessage = state.errorMessage, onLogin = viewModel::login)
        return
    }

    val loginResult = state.loginResult

    // Step 4: Admin dashboard
    if (loginResult.role == UserRole.Admin) {
        // Load teachers when admin first arrives
        LaunchedEffect(Unit) { viewModel.loadAdminTeachers() }

        AdminDashboardScreen(
            uiState = state,
            teachers = viewModel.adminTeachers,
            students = viewModel.adminStudents,
            isLoading = state.isLoading,
            onSelectTab = viewModel::selectAdminTab,
            onLogout = viewModel::logout,
            onAddTeacher = { name, subject, age, qual, exp, phone, email, ct, subjects, password, onSuccess ->
                viewModel.addTeacher(name, subject, age, qual, exp, phone, email, ct, subjects, password, onSuccess)
            },
            onAddStudent = { name, section, dob, gender, blood, address, pName, pPhone, pOcc, house, sports, bus, password, onSuccess ->
                viewModel.addStudent(name, section, dob, gender, blood, address, pName, pPhone, pOcc, house, sports, bus, password, onSuccess)
            },
            onDeleteTeacher = viewModel::deleteTeacher,
            onDeleteStudent = viewModel::deleteStudent,
            onShowSnackbar = viewModel::showSnackbar
        )
        return
    }

    val isTeacher = loginResult.role == UserRole.Teacher

    // Step 5: Attendance marking screen (teacher only)
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

    // Step 6: Filter schedule for selected day
    // For teachers: show classes where teacherId matches their userId
    // For students: show classes where classSection matches their section
    // If no classes found for teacher (new Firebase teacher not in hardcoded schedule),
    // fall back to showing all classes for the day so the UI isn't empty
    val selectedDayClasses = if (isTeacher) {
        val teacherClasses = AttendanceRepository.schedule.filter { cls ->
            cls.dayOfWeek == state.selectedDate.dayOfWeek &&
            cls.teacherId == loginResult.userId
        }.sortedBy { it.periodNumber }

        // If teacher has no classes in hardcoded schedule (new Firebase teacher),
        // show all classes for the day as a demo
        if (teacherClasses.isEmpty()) {
            AttendanceRepository.schedule.filter { cls ->
                cls.dayOfWeek == state.selectedDate.dayOfWeek
            }.sortedBy { it.periodNumber }
        } else {
            teacherClasses
        }
    } else {
        val studentSection = loginResult.classSection.ifEmpty { "10-A" }
        AttendanceRepository.schedule.filter { cls ->
            cls.dayOfWeek == state.selectedDate.dayOfWeek &&
            cls.classSection == studentSection
        }.sortedBy { it.periodNumber }
    }

    val weekDates = datesForWeek(state.selectedDate)

    // Step 7: Route to correct dashboard
    if (isTeacher) {
        TeacherDashboardScreen(
            uiState = state,
            classes = selectedDayClasses,
            weekDates = weekDates,
            loadedTeacher = viewModel.loadedTeacher,
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
