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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import com.udit.studentattendanceappication.ui.theme.AccentAmber
import com.udit.studentattendanceappication.ui.theme.CardBlue
import com.udit.studentattendanceappication.ui.theme.CardWhite
import com.udit.studentattendanceappication.ui.theme.ContentBg
import com.udit.studentattendanceappication.ui.theme.SchoolGreen
import com.udit.studentattendanceappication.ui.theme.SubjectColors
import com.udit.studentattendanceappication.ui.theme.TextPrimary
import com.udit.studentattendanceappication.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// =============================================================================
// STUDENT DASHBOARD
// =============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    uiState: AppUiState,
    classes: List<ClassSchedule>,
    weekDates: List<LocalDate>,
    onSelectTab: (DashboardTab) -> Unit,
    onLogout: () -> Unit,
    onChangeMonth: (Long) -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val loginResult = uiState.loginResult ?: return
    val freePeriods = AttendanceRepository.freePeriodsByDay(uiState.selectedDate.dayOfWeek)
    val snackbarHostState = remember { SnackbarHostState() }

    // LaunchedEffect watches snackbarMessage — whenever it changes to a non-null value,
    // it shows the snackbar. This is the standard Compose way to trigger one-time actions.
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        containerColor = ContentBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (uiState.selectedStudentTab) {
                            DashboardTab.Home       -> "Dashboard"
                            DashboardTab.MyPlan     -> "My Plan"
                            DashboardTab.Attendance -> "Attendance"
                            DashboardTab.Profile    -> "Profile"
                        },
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                actions = {
                    // Tapping the avatar navigates to the Profile tab
                    Box(
                        modifier = Modifier
                            .clickable { onSelectTab(DashboardTab.Profile) }
                            .padding(end = 16.dp)
                    ) {
                        AvatarCircle(name = loginResult.displayName, size = 36.dp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = CardWhite, tonalElevation = 0.dp) {
                listOf(
                    Triple("Home",       Icons.Outlined.Home,                   uiState.selectedStudentTab == DashboardTab.Home),
                    Triple("My Plan",    Icons.AutoMirrored.Outlined.EventNote, uiState.selectedStudentTab == DashboardTab.MyPlan),
                    Triple("Attendance", Icons.Outlined.BarChart,               uiState.selectedStudentTab == DashboardTab.Attendance),
                    Triple("Profile",    Icons.Outlined.PersonOutline,          uiState.selectedStudentTab == DashboardTab.Profile)
                ).forEachIndexed { index, (label, icon, selected) ->
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onSelectTab(DashboardTab.entries[index]) },
                        icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(22.dp)) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SchoolGreen,
                            selectedTextColor = SchoolGreen,
                            indicatorColor = SchoolGreen.copy(alpha = 0.12f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (uiState.selectedStudentTab) {
                DashboardTab.Home -> StudentHomeContent(
                    loginResult = loginResult,
                    classes = classes,
                    selectedDate = uiState.selectedDate
                )
                DashboardTab.MyPlan -> MyPlanScreen(
                    loginResult = loginResult,
                    selectedDate = uiState.selectedDate,
                    classes = classes,
                    freePeriods = freePeriods
                )
                DashboardTab.Attendance -> StudentAttendanceHistoryScreen(
                    studentId = loginResult.userId,
                    studentName = loginResult.displayName
                )
                DashboardTab.Profile -> StudentProfileScreen(
                    loginResult = loginResult,
                    onLogout = onLogout,
                    onShowSnackbar = onShowSnackbar
                )
            }
        }
    }
}

// =============================================================================
// STUDENT HOME — shows today's schedule with clean subject cards (no emojis)
// =============================================================================

@Composable
fun StudentHomeContent(
    loginResult: LoginResult,
    classes: List<ClassSchedule>,
    selectedDate: LocalDate
) {
    val student = AttendanceRepository.students.firstOrNull { it.id == loginResult.userId }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Greeting banner with student photo placeholder
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SchoolGreen)
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Photo placeholder
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = loginResult.displayName.first().uppercase(),
                            color = SchoolGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            "Good Morning,",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            loginResult.displayName,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        Text(
                            "Class ${loginResult.classSection}  |  Roll No. ${student?.rollNumber ?: ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }
            }
        }

        // Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard2(Modifier.weight(1f), "${classes.size}", "Classes Today", SchoolGreen)
                StatCard2(Modifier.weight(1f), loginResult.classSection, "My Class", CardBlue)
                StatCard2(
                    Modifier.weight(1f),
                    student?.sportsHouse?.replace(" House", "") ?: "--",
                    "House",
                    AccentAmber
                )
            }
        }

        item {
            Text(
                "Today's Schedule",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        if (classes.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "No classes today",
                    subtitle = "Nothing scheduled for this day."
                )
            }
        } else {
            val rows = classes.chunked(2)
            items(rows) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { classInfo ->
                        StudentSubjectCard(
                            modifier = Modifier.weight(1f),
                            classInfo = classInfo,
                            cardColor = SubjectColors[classes.indexOf(classInfo) % SubjectColors.size]
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun StatCard2(modifier: Modifier, value: String, label: String, color: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

// Student subject card — no emojis, clean text-based design
@Composable
private fun StudentSubjectCard(
    modifier: Modifier,
    classInfo: ClassSchedule,
    cardColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Period + room
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    "Period ${classInfo.periodNumber}",
                    color = Color.White.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White.copy(alpha = 0.22f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        classInfo.room,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Subject name
            Text(
                classInfo.subject,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall
            )

            // Time — plain hyphen, no special characters
            Text(
                "${classInfo.startTime} - ${classInfo.endTime}",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodySmall
            )

            // Teacher name
            Text(
                "By ${AttendanceRepository.teacherName(classInfo.teacherId)}",
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// =============================================================================
// STUDENT PROFILE — full student info with photo placeholder
// =============================================================================

@Composable
fun StudentProfileScreen(
    loginResult: LoginResult,
    onLogout: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val student = AttendanceRepository.students.firstOrNull { it.id == loginResult.userId }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile header with photo placeholder
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SchoolGreen)
                    .padding(24.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Photo placeholder
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = loginResult.displayName.first().uppercase(),
                            color = SchoolGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            loginResult.displayName,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Class ${loginResult.classSection}  |  Roll ${student?.rollNumber ?: ""}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        if (student?.sportsHouse?.isNotEmpty() == true) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    student.sportsHouse,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

        // Personal info
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Personal Information",
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    ProfileRow("Student ID", loginResult.userId)
                    ProfileRow("Roll Number", student?.rollNumber ?: "--")
                    ProfileRow("Date of Birth", student?.dateOfBirth ?: "--")
                    ProfileRow("Gender", student?.gender ?: "--")
                    ProfileRow("Blood Group", student?.bloodGroup ?: "--")
                    ProfileRow("Admission Year", student?.admissionYear ?: "--")
                }
            }
        }

        // Parent / Guardian info
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Parent / Guardian",
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    ProfileRow("Parent Name", student?.parentName ?: "--")
                    ProfileRow("Parent Phone", student?.parentPhone ?: "--")
                    ProfileRow("Occupation", student?.parentOccupation ?: "--")
                    ProfileRow("Address", student?.address ?: "--")
                }
            }
        }

        // School activities
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "School Activities",
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    ProfileRow("Sports House", student?.sportsHouse ?: "--")
                    ProfileRow("Sports", student?.sports ?: "--")
                    ProfileRow("Bus Route", student?.busRoute ?: "--")
                }
            }
        }

        // Action buttons — all show snackbar feedback
        item {
            Button(
                onClick = { onShowSnackbar("Leave application submitted successfully") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SchoolGreen.copy(alpha = 0.12f),
                    contentColor = SchoolGreen
                )
            ) {
                Text("Apply for Leave", fontWeight = FontWeight.SemiBold)
            }
        }

        item {
            Button(
                onClick = { onShowSnackbar("Fee receipt sent to parent's email") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SchoolGreen.copy(alpha = 0.12f),
                    contentColor = SchoolGreen
                )
            ) {
                Text("Download Fee Receipt", fontWeight = FontWeight.SemiBold)
            }
        }

        item {
            Button(
                onClick = { onShowSnackbar("Result card will be available after exams") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SchoolGreen.copy(alpha = 0.12f),
                    contentColor = SchoolGreen
                )
            ) {
                Text("View Result Card", fontWeight = FontWeight.SemiBold)
            }
        }

        item {
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SchoolGreen,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }
    }
}
