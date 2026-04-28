package com.udit.studentattendanceappication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udit.studentattendanceappication.ui.components.AvatarCircle
import com.udit.studentattendanceappication.ui.components.EmptyStateCard
import com.udit.studentattendanceappication.ui.components.ProfileRow
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.model.AppUiState
import com.udit.studentattendanceappication.ui.model.ClassSchedule
import com.udit.studentattendanceappication.ui.model.DashboardTab
import com.udit.studentattendanceappication.ui.model.LoginResult
import com.udit.studentattendanceappication.ui.model.TeacherTab
import com.udit.studentattendanceappication.ui.model.UserRole
import com.udit.studentattendanceappication.ui.theme.AccentAmber
import com.udit.studentattendanceappication.ui.theme.CardBlue
import com.udit.studentattendanceappication.ui.theme.CardWhite
import com.udit.studentattendanceappication.ui.theme.ContentBg
import com.udit.studentattendanceappication.ui.theme.DividerColor
import com.udit.studentattendanceappication.ui.theme.SchoolGreen
import com.udit.studentattendanceappication.ui.theme.SchoolGreenLight
import com.udit.studentattendanceappication.ui.theme.SubjectColors
import com.udit.studentattendanceappication.ui.theme.TextPrimary
import com.udit.studentattendanceappication.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter


// ── Login ─────────────────────────────────────────────────────────────────────

@Composable
fun LoginScreen(errorMessage: String?, onLogin: (String, String) -> Unit) {
    var userId by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize().background(SchoolGreen), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Welcome", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 38.sp)
            Spacer(Modifier.height(6.dp))
            Text("Sign in to your account", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            Spacer(Modifier.height(40.dp))
            Text("User ID *", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = userId, onValueChange = { userId = it },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                placeholder = { Text("Enter User ID", color = Color.White.copy(alpha = 0.5f)) },
                shape = RoundedCornerShape(14.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White.copy(alpha = 0.8f), unfocusedBorderColor = Color.White.copy(alpha = 0.35f),
                    cursorColor = Color.White, focusedContainerColor = Color.White.copy(alpha = 0.12f), unfocusedContainerColor = Color.White.copy(alpha = 0.08f)
                )
            )
            Spacer(Modifier.height(20.dp))
            Text("Password *", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                placeholder = { Text("Enter Password", color = Color.White.copy(alpha = 0.5f)) },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(14.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White.copy(alpha = 0.8f), unfocusedBorderColor = Color.White.copy(alpha = 0.35f),
                    cursorColor = Color.White, focusedContainerColor = Color.White.copy(alpha = 0.12f), unfocusedContainerColor = Color.White.copy(alpha = 0.08f)
                )
            )
            if (errorMessage != null) {
                Spacer(Modifier.height(10.dp))
                Text(errorMessage, color = Color(0xFFFFCDD2), style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth())
            }
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = { onLogin(userId.trim(), password.trim()) },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.22f), contentColor = Color.White)
            ) { Text("Log In", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
        }
    }
}

// ── Bottom Nav ────────────────────────────────────────────────────────────────

private data class NavItem(val label: String, val icon: ImageVector, val selected: Boolean)

@Composable
private fun AppBottomNav(items: List<NavItem>, onSelect: (Int) -> Unit) {
    NavigationBar(containerColor = CardWhite, tonalElevation = 0.dp) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = item.selected, onClick = { onSelect(index) },
                icon = { Icon(item.icon, contentDescription = item.label, modifier = Modifier.size(22.dp)) },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall, fontWeight = if (item.selected) FontWeight.SemiBold else FontWeight.Normal) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SchoolGreen, selectedTextColor = SchoolGreen,
                    indicatorColor = SchoolGreen.copy(alpha = 0.12f),
                    unselectedIconColor = TextSecondary, unselectedTextColor = TextSecondary
                )
            )
        }
    }
}

// ── Teacher Dashboard ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    uiState: AppUiState, classes: List<ClassSchedule>, weekDates: List<LocalDate>,
    onSelectTab: (TeacherTab) -> Unit, onLogout: () -> Unit,
    onChangeMonth: (Long) -> Unit, onSelectDate: (LocalDate) -> Unit,
    onTakeAttendance: (String) -> Unit, onShowSnackbar: (String) -> Unit
) {
    val loginResult = uiState.loginResult ?: return
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { snackbarHostState.showSnackbar(it) }
    }
    Scaffold(
        containerColor = ContentBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(when (uiState.selectedTeacherTab) { TeacherTab.Home -> "Dashboard"; TeacherTab.Calendar -> "Calendar"; TeacherTab.Profile -> "Profile" }, fontWeight = FontWeight.Bold, color = TextPrimary) },
                actions = { AvatarCircle(name = loginResult.displayName, size = 36.dp); Spacer(Modifier.width(16.dp)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        },
        bottomBar = {
            AppBottomNav(
                items = listOf(
                    NavItem("Home", Icons.Outlined.Home, uiState.selectedTeacherTab == TeacherTab.Home),
                    NavItem("Calendar", Icons.Outlined.CalendarMonth, uiState.selectedTeacherTab == TeacherTab.Calendar),
                    NavItem("Profile", Icons.Outlined.PersonOutline, uiState.selectedTeacherTab == TeacherTab.Profile)
                ),
                onSelect = { onSelectTab(TeacherTab.entries[it]) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (uiState.selectedTeacherTab) {
                TeacherTab.Home -> TeacherHomeContent(loginResult, classes, uiState.selectedDate, onTakeAttendance, onShowSnackbar)
                TeacherTab.Calendar -> CalendarScreen(
                    isTeacher = true, selectedMonth = uiState.selectedMonth,
                    selectedDate = uiState.selectedDate, weekDates = weekDates, classes = classes,
                    onChangeMonth = onChangeMonth, onSelectDate = onSelectDate,
                    onTakeAttendance = onTakeAttendance, onShowSnackbar = onShowSnackbar
                )
                TeacherTab.Profile -> TeacherProfileScreen(loginResult, onLogout, onShowSnackbar)
            }
        }
    }
}

// ── Student Dashboard ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    uiState: AppUiState, classes: List<ClassSchedule>, weekDates: List<LocalDate>,
    onSelectTab: (DashboardTab) -> Unit, onLogout: () -> Unit,
    onChangeMonth: (Long) -> Unit, onSelectDate: (LocalDate) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val loginResult = uiState.loginResult ?: return
    val freePeriods = AttendanceRepository.freePeriodsByDay(uiState.selectedDate.dayOfWeek)
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { snackbarHostState.showSnackbar(it) }
    }
    Scaffold(
        containerColor = ContentBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(when (uiState.selectedStudentTab) { DashboardTab.Home -> "Dashboard"; DashboardTab.MyPlan -> "My Plan"; DashboardTab.Attendance -> "Attendance"; DashboardTab.Profile -> "Profile" }, fontWeight = FontWeight.Bold, color = TextPrimary) },
                actions = { AvatarCircle(name = loginResult.displayName, size = 36.dp); Spacer(Modifier.width(16.dp)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        },
        bottomBar = {
            AppBottomNav(
                items = listOf(
                    NavItem("Home", Icons.Outlined.Home, uiState.selectedStudentTab == DashboardTab.Home),
                    NavItem("My Plan", Icons.AutoMirrored.Outlined.EventNote, uiState.selectedStudentTab == DashboardTab.MyPlan),
                    NavItem("Attendance", Icons.Outlined.BarChart, uiState.selectedStudentTab == DashboardTab.Attendance),
                    NavItem("Profile", Icons.Outlined.PersonOutline, uiState.selectedStudentTab == DashboardTab.Profile)
                ),
                onSelect = { onSelectTab(DashboardTab.entries[it]) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (uiState.selectedStudentTab) {
                DashboardTab.Home -> StudentHomeContent(loginResult, classes, uiState.selectedDate, onShowSnackbar)
                DashboardTab.MyPlan -> MyPlanScreen(loginResult = loginResult, selectedDate = uiState.selectedDate, classes = classes, freePeriods = freePeriods)
                DashboardTab.Attendance -> StudentAttendanceHistoryScreen(studentId = loginResult.userId, studentName = loginResult.displayName)
                DashboardTab.Profile -> StudentProfileScreen(loginResult, onLogout, onShowSnackbar)
            }
        }
    }
}


// ── Teacher Home ──────────────────────────────────────────────────────────────

@Composable
private fun TeacherHomeContent(
    loginResult: LoginResult, classes: List<ClassSchedule>,
    selectedDate: LocalDate, onTakeAttendance: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val teacher = AttendanceRepository.teachers.firstOrNull { it.id == loginResult.userId }
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(SchoolGreen).padding(20.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Profile photo placeholder
                    Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.25f)), contentAlignment = Alignment.Center) {
                        Text(loginResult.displayName.first().uppercase(), color = SchoolGreen, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("Good Morning,", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                        Text(loginResult.displayName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
                        Text(selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")), style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.75f))
                    }
                }
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard(Modifier.weight(1f), "${classes.size}", "Classes Today", SchoolGreen)
                StatCard(Modifier.weight(1f), teacher?.classTeacherOf?.ifEmpty { "—" } ?: "—", "Class Teacher", CardBlue)
                StatCard(Modifier.weight(1f), teacher?.experience ?: "—", "Experience", AccentAmber)
            }
        }
        item { Text("Today's Classes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary) }
        if (classes.isEmpty()) {
            item { EmptyStateCard("No classes today", "Nothing scheduled for this day.") }
        } else {
            val rows = classes.chunked(2)
            items(rows) { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { cls ->
                        SubjectCard(Modifier.weight(1f), cls, SubjectColors[classes.indexOf(cls) % SubjectColors.size], true, onTakeAttendance)
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

// ── Student Home ──────────────────────────────────────────────────────────────

@Composable
private fun StudentHomeContent(
    loginResult: LoginResult, classes: List<ClassSchedule>,
    selectedDate: LocalDate, onShowSnackbar: (String) -> Unit
) {
    val student = AttendanceRepository.students.firstOrNull { it.id == loginResult.userId }
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(SchoolGreen).padding(20.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.25f)), contentAlignment = Alignment.Center) {
                        Text(loginResult.displayName.first().uppercase(), color = SchoolGreen, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("Good Morning,", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                        Text(loginResult.displayName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
                        Text("Class ${loginResult.classSection}  •  Roll No. ${student?.rollNumber ?: ""}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.75f))
                    }
                }
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard(Modifier.weight(1f), "${classes.size}", "Classes Today", SchoolGreen)
                StatCard(Modifier.weight(1f), loginResult.classSection, "My Class", CardBlue)
                StatCard(Modifier.weight(1f), student?.sportsHouse?.replace(" House","") ?: "—", "House", AccentAmber)
            }
        }
        item { Text("Today's Schedule", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary) }
        if (classes.isEmpty()) {
            item { EmptyStateCard("No classes today", "Nothing scheduled for this day.") }
        } else {
            val rows = classes.chunked(2)
            items(rows) { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { cls ->
                        SubjectCard(Modifier.weight(1f), cls, SubjectColors[classes.indexOf(cls) % SubjectColors.size], false) {}
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

// ── Shared stat card ──────────────────────────────────────────────────────────

@Composable
private fun StatCard(modifier: Modifier, value: String, label: String, color: Color) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = CardWhite), shape = RoundedCornerShape(14.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

// ── Subject card — no emojis, clean school style ──────────────────────────────

@Composable
private fun SubjectCard(modifier: Modifier, classInfo: ClassSchedule, cardColor: Color, isTeacher: Boolean, onTakeAttendance: (String) -> Unit) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = cardColor), shape = RoundedCornerShape(18.dp), elevation = CardDefaults.cardElevation(3.dp)) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Text("Period ${classInfo.periodNumber}", color = Color.White.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall)
                Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(Color.White.copy(alpha = 0.22f)).padding(horizontal = 6.dp, vertical = 3.dp)) {
                    Text(classInfo.room, color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
            Text(classInfo.subject, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleSmall, lineHeight = 20.sp)
            Text("${classInfo.startTime} – ${classInfo.endTime}", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
            Text(
                if (isTeacher) "Class ${classInfo.classSection}  •  ${AttendanceRepository.studentsInSection(classInfo.classSection).size} Students"
                else AttendanceRepository.teacherName(classInfo.teacherId),
                color = Color.White.copy(alpha = 0.75f), style = MaterialTheme.typography.bodySmall
            )
            if (isTeacher) {
                Spacer(Modifier.height(2.dp))
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color.White.copy(alpha = 0.2f)).clickable { onTakeAttendance(classInfo.id) }.padding(vertical = 9.dp), contentAlignment = Alignment.Center) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                        Text("Mark Attendance", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ── Teacher Profile ───────────────────────────────────────────────────────────

@Composable
private fun TeacherProfileScreen(loginResult: LoginResult, onLogout: () -> Unit, onShowSnackbar: (String) -> Unit) {
    val teacher = AttendanceRepository.teachers.firstOrNull { it.id == loginResult.userId }
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(SchoolGreen).padding(24.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Profile photo placeholder circle
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.25f)), contentAlignment = Alignment.Center) {
                        Text(loginResult.displayName.first().uppercase(), color = SchoolGreen, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(loginResult.displayName, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleLarge)
                        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color.White.copy(alpha = 0.2f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                            Text("Teacher", color = Color.White, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        }
                        if (teacher?.classTeacherOf?.isNotEmpty() == true) {
                            Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color.White.copy(alpha = 0.15f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                                Text("Class Teacher — ${teacher.classTeacherOf}", color = Color.White, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite), elevation = CardDefaults.cardElevation(1.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Personal Information", fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(2.dp))
                    ProfileRow("Employee ID", loginResult.userId)
                    ProfileRow("Age", "${teacher?.age ?: "—"} years")
                    ProfileRow("Qualification", teacher?.qualification ?: "—")
                    ProfileRow("Experience", teacher?.experience ?: "—")
                    ProfileRow("Joined", teacher?.joinYear ?: "—")
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite), elevation = CardDefaults.cardElevation(1.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("School Information", fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(2.dp))
                    ProfileRow("Primary Subject", teacher?.subject ?: "—")
                    ProfileRow("Subjects Taught", teacher?.subjectsTaught?.joinToString(", ") ?: "—")
                    ProfileRow("Class Teacher Of", teacher?.classTeacherOf?.ifEmpty { "Not assigned" } ?: "Not assigned")
                    ProfileRow("Phone", teacher?.phone ?: "—")
                    ProfileRow("Email", teacher?.email ?: "—")
                }
            }
        }
        item {
            Button(onClick = { onShowSnackbar("Notification settings coming soon") }, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen.copy(alpha = 0.12f), contentColor = SchoolGreen)) {
                Text("Notification Settings", fontWeight = FontWeight.SemiBold)
            }
        }
        item {
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen, contentColor = Color.White)) {
                Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }
    }
}

// ── Student Profile ───────────────────────────────────────────────────────────

@Composable
private fun StudentProfileScreen(loginResult: LoginResult, onLogout: () -> Unit, onShowSnackbar: (String) -> Unit) {
    val student = AttendanceRepository.students.firstOrNull { it.id == loginResult.userId }
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(SchoolGreen).padding(24.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.25f)), contentAlignment = Alignment.Center) {
                        Text(loginResult.displayName.first().uppercase(), color = SchoolGreen, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(loginResult.displayName, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleLarge)
                        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color.White.copy(alpha = 0.2f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                            Text("Class ${loginResult.classSection}  •  Roll ${student?.rollNumber ?: ""}", color = Color.White, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        }
                        if (student?.sportsHouse?.isNotEmpty() == true) {
                            Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color.White.copy(alpha = 0.15f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                                Text(student.sportsHouse, color = Color.White, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite), elevation = CardDefaults.cardElevation(1.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Personal Information", fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(2.dp))
                    ProfileRow("Student ID", loginResult.userId)
                    ProfileRow("Roll Number", student?.rollNumber ?: "—")
                    ProfileRow("Date of Birth", student?.dateOfBirth ?: "—")
                    ProfileRow("Gender", student?.gender ?: "—")
                    ProfileRow("Blood Group", student?.bloodGroup ?: "—")
                    ProfileRow("Admission Year", student?.admissionYear ?: "—")
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite), elevation = CardDefaults.cardElevation(1.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Parent / Guardian", fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(2.dp))
                    ProfileRow("Parent Name", student?.parentName ?: "—")
                    ProfileRow("Parent Phone", student?.parentPhone ?: "—")
                    ProfileRow("Occupation", student?.parentOccupation ?: "—")
                    ProfileRow("Address", student?.address ?: "—")
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite), elevation = CardDefaults.cardElevation(1.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("School Activities", fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(2.dp))
                    ProfileRow("Sports House", student?.sportsHouse ?: "—")
                    ProfileRow("Sports", student?.sports ?: "—")
                    ProfileRow("Bus Route", student?.busRoute ?: "—")
                }
            }
        }
        item {
            Button(onClick = { onShowSnackbar("Leave application submitted") }, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen.copy(alpha = 0.12f), contentColor = SchoolGreen)) {
                Text("Apply for Leave", fontWeight = FontWeight.SemiBold)
            }
        }
        item {
            Button(onClick = { onShowSnackbar("Fee receipt sent to parent's email") }, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen.copy(alpha = 0.12f), contentColor = SchoolGreen)) {
                Text("Download Fee Receipt", fontWeight = FontWeight.SemiBold)
            }
        }
        item {
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen, contentColor = Color.White)) {
                Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }
    }
}
