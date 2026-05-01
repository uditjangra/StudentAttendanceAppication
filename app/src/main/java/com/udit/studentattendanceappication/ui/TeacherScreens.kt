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
import androidx.compose.material.icons.automirrored.outlined.Logout
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
import com.udit.studentattendanceappication.ui.model.LoginResult
import com.udit.studentattendanceappication.ui.model.TeacherTab
import com.udit.studentattendanceappication.ui.theme.AccentAmber
import com.udit.studentattendanceappication.ui.theme.CardBlue
import com.udit.studentattendanceappication.ui.theme.CardWhite
import com.udit.studentattendanceappication.ui.theme.ContentBg
import com.udit.studentattendanceappication.ui.theme.DividerColor
import com.udit.studentattendanceappication.ui.theme.SchoolGreen
import com.udit.studentattendanceappication.ui.theme.SubjectColors
import com.udit.studentattendanceappication.ui.theme.TextPrimary
import com.udit.studentattendanceappication.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// =============================================================================
// TEACHER DASHBOARD
// =============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    uiState: AppUiState,
    classes: List<ClassSchedule>,
    weekDates: List<LocalDate>,
    onSelectTab: (TeacherTab) -> Unit,
    onLogout: () -> Unit,
    onChangeMonth: (Long) -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onTakeAttendance: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val loginResult = uiState.loginResult ?: return
    val snackbarHostState = remember { SnackbarHostState() }

    // Show snackbar when message changes
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
                        when (uiState.selectedTeacherTab) {
                            TeacherTab.Home     -> "Dashboard"
                            TeacherTab.Calendar -> "Calendar"
                            TeacherTab.Profile  -> "Profile"
                        },
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                actions = {
                    AvatarCircle(name = loginResult.displayName, size = 36.dp)
                    Spacer(Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = CardWhite, tonalElevation = 0.dp) {
                listOf(
                    Triple("Home",     Icons.Outlined.Home,          uiState.selectedTeacherTab == TeacherTab.Home),
                    Triple("Calendar", Icons.Outlined.CalendarMonth, uiState.selectedTeacherTab == TeacherTab.Calendar),
                    Triple("Profile",  Icons.Outlined.PersonOutline, uiState.selectedTeacherTab == TeacherTab.Profile)
                ).forEachIndexed { index, (label, icon, selected) ->
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onSelectTab(TeacherTab.entries[index]) },
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
            when (uiState.selectedTeacherTab) {
                TeacherTab.Home -> TeacherHomeContent(
                    loginResult = loginResult,
                    classes = classes,
                    selectedDate = uiState.selectedDate,
                    onTakeAttendance = onTakeAttendance,
                    onShowSnackbar = onShowSnackbar
                )
                TeacherTab.Calendar -> CalendarScreen(
                    isTeacher = true,
                    selectedMonth = uiState.selectedMonth,
                    selectedDate = uiState.selectedDate,
                    weekDates = weekDates,
                    classes = classes,
                    onChangeMonth = onChangeMonth,
                    onSelectDate = onSelectDate,
                    onTakeAttendance = onTakeAttendance,
                    onShowSnackbar = onShowSnackbar
                )
                TeacherTab.Profile -> TeacherProfileScreen(
                    loginResult = loginResult,
                    onLogout = onLogout,
                    onShowSnackbar = onShowSnackbar
                )
            }
        }
    }
}

// =============================================================================
// TEACHER HOME — shows all classes for the day with subject cards
// =============================================================================

@Composable
fun TeacherHomeContent(
    loginResult: LoginResult,
    classes: List<ClassSchedule>,
    selectedDate: LocalDate,
    onTakeAttendance: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val teacher = AttendanceRepository.teachers.firstOrNull { it.id == loginResult.userId }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Greeting banner with teacher photo placeholder
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
                    // Profile photo placeholder (initials circle)
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
                            selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }
            }
        }

        // Stats row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(Modifier.weight(1f), "${classes.size}", "Classes Today", SchoolGreen)
                StatCard(
                    Modifier.weight(1f),
                    teacher?.classTeacherOf?.ifEmpty { "None" } ?: "None",
                    "Class Teacher",
                    CardBlue
                )
                StatCard(Modifier.weight(1f), teacher?.experience ?: "--", "Experience", AccentAmber)
            }
        }

        // Section title
        item {
            Text(
                "Today's Classes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Class cards in 2-column grid
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
                        TeacherSubjectCard(
                            modifier = Modifier.weight(1f),
                            classInfo = classInfo,
                            cardColor = SubjectColors[classes.indexOf(classInfo) % SubjectColors.size],
                            onTakeAttendance = onTakeAttendance
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier, value: String, label: String, color: Color) {
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

// Subject card for teacher — shows period, room, class section, Mark Attendance button
@Composable
private fun TeacherSubjectCard(
    modifier: Modifier,
    classInfo: ClassSchedule,
    cardColor: Color,
    onTakeAttendance: (String) -> Unit
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
            // Period number + room badge
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

            // Time — using plain hyphen to avoid encoding issues
            Text(
                "${classInfo.startTime} - ${classInfo.endTime}",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodySmall
            )

            // Class section and student count
            Text(
                "Class ${classInfo.classSection}  |  ${AttendanceRepository.studentsInSection(classInfo.classSection).size} Students",
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(2.dp))

            // Mark Attendance button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { onTakeAttendance(classInfo.id) }
                    .padding(vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        "Mark Attendance",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// =============================================================================
// TEACHER PROFILE — full teacher info with photo placeholder
// =============================================================================

@Composable
fun TeacherProfileScreen(
    loginResult: LoginResult,
    onLogout: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val teacher = AttendanceRepository.teachers.firstOrNull { it.id == loginResult.userId }

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
                        Text(
                            "Employee ID: ${loginResult.userId}",
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Teacher",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Personal info card
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
                    ProfileRow("Employee ID", loginResult.userId)
                    ProfileRow("Age", "${teacher?.age ?: "--"} years")
                    ProfileRow("Qualification", teacher?.qualification ?: "--")
                    ProfileRow("Experience", teacher?.experience ?: "--")
                    ProfileRow("Joined", teacher?.joinYear ?: "--")
                    ProfileRow("Phone", teacher?.phone ?: "--")
                    ProfileRow("Email", teacher?.email ?: "--")
                }
            }
        }

        // School info card
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
                        "School Information",
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    ProfileRow("Primary Subject", teacher?.subject ?: "--")
                    ProfileRow(
                        "Subjects Taught",
                        teacher?.subjectsTaught?.joinToString(", ") ?: "--"
                    )
                    ProfileRow(
                        "Class Teacher Of",
                        teacher?.classTeacherOf?.ifEmpty { "Not assigned" } ?: "Not assigned"
                    )
                }
            }
        }

        // Action buttons — all show snackbar feedback
        item {
            Button(
                onClick = { onShowSnackbar("Notification settings updated") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SchoolGreen.copy(alpha = 0.12f),
                    contentColor = SchoolGreen
                )
            ) {
                Text("Notification Settings", fontWeight = FontWeight.SemiBold)
            }
        }

        item {
            Button(
                onClick = { onShowSnackbar("Attendance report downloaded") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SchoolGreen.copy(alpha = 0.12f),
                    contentColor = SchoolGreen
                )
            ) {
                Text("Download Attendance Report", fontWeight = FontWeight.SemiBold)
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
