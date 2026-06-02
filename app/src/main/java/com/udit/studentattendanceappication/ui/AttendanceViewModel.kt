package com.udit.studentattendanceappication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.data.FirebaseService
import com.udit.studentattendanceappication.ui.model.AdminTab
import com.udit.studentattendanceappication.ui.model.AppUiState
import com.udit.studentattendanceappication.ui.model.DashboardTab
import com.udit.studentattendanceappication.ui.model.LoginResult
import com.udit.studentattendanceappication.ui.model.Student
import com.udit.studentattendanceappication.ui.model.Teacher
import com.udit.studentattendanceappication.ui.model.TeacherTab
import com.udit.studentattendanceappication.ui.model.UserRole
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class AttendanceViewModel : ViewModel() {

    var uiState by mutableStateOf(AppUiState())
        private set

    // Splash screen flag
    var showSplash by mutableStateOf(true)
        private set

    // Store admin credentials to re-sign in after creating teacher/student accounts
    private var adminEmail = ""
    private var adminPassword = ""

    // Attendance storage: key = "classId|date" -> Map<studentId, Boolean>
    private val attendanceState = mutableStateMapOf<String, MutableMap<String, Boolean>>()

    // Loaded teacher/student profile from Firebase (null until loaded)
    var loadedTeacher by mutableStateOf<Teacher?>(null)
        private set
    var loadedStudent by mutableStateOf<Student?>(null)
        private set

    // Admin panel lists
    var adminTeachers by mutableStateOf<List<Teacher>>(emptyList())
        private set
    var adminStudents by mutableStateOf<List<Student>>(emptyList())
        private set

    fun splashFinished() { showSplash = false }

    // ── Login ─────────────────────────────────────────────────────────────────

    fun login(userId: String, password: String) {
        if (userId.isBlank() || password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Please enter User ID and Password.")
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val (role, uid) = FirebaseService.login(userId.trim(), password.trim())

                when (role) {
                    "admin" -> {
                        adminEmail = userId.trim()
                        adminPassword = password.trim()
                        uiState = AppUiState(
                            loginResult = LoginResult(UserRole.Admin, userId, "Admin"),
                            isLoading = false
                        )
                    }
                    "teacher" -> {
                        val teacher = FirebaseService.fetchTeacher(userId)
                        loadedTeacher = teacher
                        uiState = AppUiState(
                            loginResult = LoginResult(
                                role = UserRole.Teacher,
                                userId = userId,
                                displayName = teacher?.name ?: userId
                            ),
                            selectedDate = LocalDate.now(),
                            selectedMonth = YearMonth.now(),
                            isLoading = false
                        )
                    }
                    "student" -> {
                        val student = FirebaseService.fetchStudent(userId)
                        loadedStudent = student
                        uiState = AppUiState(
                            loginResult = LoginResult(
                                role = UserRole.Student,
                                userId = userId,
                                displayName = student?.name ?: userId,
                                classSection = student?.classSection ?: ""
                            ),
                            selectedDate = LocalDate.now(),
                            selectedMonth = YearMonth.now(),
                            isLoading = false
                        )
                    }
                    else -> {
                        uiState = uiState.copy(
                            isLoading = false,
                            errorMessage = "Unknown role. Contact admin."
                        )
                    }
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Login failed: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        FirebaseService.logout()
        loadedTeacher = null
        loadedStudent = null
        uiState = AppUiState()
    }

    // ── Admin ─────────────────────────────────────────────────────────────────

    fun selectAdminTab(tab: AdminTab) {
        uiState = uiState.copy(selectedAdminTab = tab)
        if (tab == AdminTab.Teachers) loadAdminTeachers()
        else loadAdminStudents()
    }

    fun loadAdminTeachers() {
        viewModelScope.launch {
            try {
                adminTeachers = FirebaseService.fetchAllTeachers()
            } catch (e: Exception) {
                showSnackbar("Failed to load teachers: ${e.message}")
            }
        }
    }

    fun loadAdminStudents() {
        viewModelScope.launch {
            try {
                adminStudents = FirebaseService.fetchAllStudents()
            } catch (e: Exception) {
                showSnackbar("Failed to load students: ${e.message}")
            }
        }
    }

    fun addTeacher(
        name: String, subject: String, age: String, qualification: String,
        experience: String, phone: String, email: String,
        classTeacherOf: String, subjectsTaught: String, initialPassword: String,
        onSuccess: (userId: String, password: String) -> Unit
    ) {
        uiState = uiState.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val (userId, password) = FirebaseService.addTeacher(
                    adminEmail, adminPassword,
                    name, subject, age, qualification, experience,
                    phone, email, classTeacherOf, subjectsTaught, initialPassword
                )
                uiState = uiState.copy(isLoading = false)
                loadAdminTeachers()
                onSuccess(userId, password)
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false)
                showSnackbar("Failed to add teacher: ${e.message}")
            }
        }
    }

    fun addStudent(
        name: String, classSection: String, dateOfBirth: String, gender: String,
        bloodGroup: String, address: String, parentName: String, parentPhone: String,
        parentOccupation: String, sportsHouse: String, sports: String, busRoute: String,
        initialPassword: String,
        onSuccess: (userId: String, password: String) -> Unit
    ) {
        uiState = uiState.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val (userId, password) = FirebaseService.addStudent(
                    adminEmail, adminPassword,
                    name, classSection, dateOfBirth, gender, bloodGroup,
                    address, parentName, parentPhone, parentOccupation,
                    sportsHouse, sports, busRoute, initialPassword
                )
                uiState = uiState.copy(isLoading = false)
                loadAdminStudents()
                onSuccess(userId, password)
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false)
                showSnackbar("Failed to add student: ${e.message}")
            }
        }
    }

    fun deleteTeacher(userId: String) {
        viewModelScope.launch {
            try {
                FirebaseService.deleteTeacher(userId)
                loadAdminTeachers()
                showSnackbar("Teacher removed")
            } catch (e: Exception) {
                showSnackbar("Failed to delete: ${e.message}")
            }
        }
    }

    fun deleteStudent(userId: String) {
        viewModelScope.launch {
            try {
                FirebaseService.deleteStudent(userId)
                loadAdminStudents()
                showSnackbar("Student removed")
            } catch (e: Exception) {
                showSnackbar("Failed to delete: ${e.message}")
            }
        }
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    fun selectStudentTab(tab: DashboardTab) { uiState = uiState.copy(selectedStudentTab = tab) }
    fun selectTeacherTab(tab: TeacherTab)   { uiState = uiState.copy(selectedTeacherTab = tab) }
    fun openAttendance(classId: String)     { uiState = uiState.copy(selectedClassId = classId) }
    fun closeAttendance()                   { uiState = uiState.copy(selectedClassId = null) }

    // ── Calendar ──────────────────────────────────────────────────────────────

    fun changeMonth(offset: Long) {
        val nextMonth = uiState.selectedMonth.plusMonths(offset)
        val adjustedDay = minOf(uiState.selectedDate.dayOfMonth, nextMonth.lengthOfMonth())
        uiState = uiState.copy(
            selectedMonth = nextMonth,
            selectedDate = LocalDate.of(nextMonth.year, nextMonth.month, adjustedDay)
        )
    }

    fun selectDate(date: LocalDate) {
        uiState = uiState.copy(selectedDate = date, selectedMonth = YearMonth.from(date))
    }

    // ── Attendance ────────────────────────────────────────────────────────────

    private fun attendanceKey(classId: String, date: LocalDate) = "$classId|$date"

    fun markAttendance(classId: String, studentId: String, present: Boolean, date: LocalDate) {
        val key = attendanceKey(classId, date)
        val section = AttendanceRepository.schedule.firstOrNull { it.id == classId }?.classSection ?: ""
        val map = attendanceState.getOrPut(key) {
            AttendanceRepository.studentsInSection(section).associate { it.id to false }.toMutableMap()
        }
        map[studentId] = present
        attendanceState[key] = map
    }

    fun markAllPresent(classId: String, date: LocalDate) {
        val key = attendanceKey(classId, date)
        val section = AttendanceRepository.schedule.firstOrNull { it.id == classId }?.classSection ?: ""
        val map = attendanceState.getOrPut(key) {
            AttendanceRepository.studentsInSection(section).associate { it.id to false }.toMutableMap()
        }
        AttendanceRepository.studentsInSection(section).forEach { map[it.id] = true }
        attendanceState[key] = map
        showSnackbar("All students marked present")
    }

    fun attendanceFor(classId: String, date: LocalDate): Map<String, Boolean> {
        val key = attendanceKey(classId, date)
        val section = AttendanceRepository.schedule.firstOrNull { it.id == classId }?.classSection ?: ""
        return attendanceState.getOrPut(key) {
            AttendanceRepository.studentsInSection(section).associate { it.id to false }.toMutableMap()
        }
    }

    fun presentCount(classId: String, date: LocalDate): Int =
        attendanceFor(classId, date).values.count { it == true }

    fun absentCount(classId: String, date: LocalDate): Int =
        attendanceFor(classId, date).values.count { it == false }

    // ── Snackbar ──────────────────────────────────────────────────────────────

    fun showSnackbar(message: String) { uiState = uiState.copy(snackbarMessage = message) }
    fun dismissSnackbar()             { uiState = uiState.copy(snackbarMessage = null) }
}
