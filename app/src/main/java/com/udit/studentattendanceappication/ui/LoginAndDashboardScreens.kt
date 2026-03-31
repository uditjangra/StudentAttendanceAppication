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
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.udit.studentattendanceappication.ui.components.AvatarCircle
import com.udit.studentattendanceappication.ui.components.EmptyStateCard
import com.udit.studentattendanceappication.ui.components.PrimaryAccentButton
import com.udit.studentattendanceappication.ui.components.ProfileRow
import com.udit.studentattendanceappication.ui.components.SoftBadge
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.model.AppUiState
import com.udit.studentattendanceappication.ui.model.ClassSchedule
import com.udit.studentattendanceappication.ui.model.DashboardTab
import com.udit.studentattendanceappication.ui.model.LoginResult
import com.udit.studentattendanceappication.ui.model.UserRole
import com.udit.studentattendanceappication.ui.theme.AccentAmber
import com.udit.studentattendanceappication.ui.theme.AttendanceAbsent
import com.udit.studentattendanceappication.ui.theme.CardDark
import com.udit.studentattendanceappication.ui.theme.MutedText
import com.udit.studentattendanceappication.ui.theme.ScreenBackground
import com.udit.studentattendanceappication.ui.theme.TealPrimary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun LoginScreen(
    errorMessage: String?,
    onLogin: (String, String) -> Unit
) {
    var userId by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ScreenBackground, Color(0xFFFDF7F0))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Attendance, organized simply.",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Log in as teacher or student to view schedules, daily cards, and attendance actions.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText
            )
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SoftBadge(icon = Icons.AutoMirrored.Outlined.Login, text = "Static login demo")
                    OutlinedTextField(
                        value = userId,
                        onValueChange = { userId = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("User ID") },
                        placeholder = { Text("Teacher: 10000 / Student: 12301") },
                        shape = RoundedCornerShape(18.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(18.dp)
                    )
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = AttendanceAbsent,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Button(
                        onClick = { onLogin(userId.trim(), password.trim()) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Text("Login")
                    }
                    Text(
                        text = "Password for all users: password123",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    uiState: AppUiState,
    classes: List<ClassSchedule>,
    weekDates: List<LocalDate>,
    onSelectTab: (DashboardTab) -> Unit,
    onLogout: () -> Unit,
    onChangeMonth: (Long) -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onTakeAttendance: (String) -> Unit
) {
    val loginResult = uiState.loginResult ?: return
    val isTeacher = loginResult.role == UserRole.Teacher

    Scaffold(
        containerColor = ScreenBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (uiState.selectedTab) {
                        DashboardTab.Home,
                        DashboardTab.Calendar -> {
                            if (isTeacher) {
                                classes.firstOrNull()?.let { onTakeAttendance(it.id) }
                            } else {
                                onSelectTab(DashboardTab.Calendar)
                            }
                        }

                        DashboardTab.Profile -> onLogout()
                    }
                },
                containerColor = AccentAmber,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = if (uiState.selectedTab == DashboardTab.Profile) Icons.AutoMirrored.Outlined.Logout else Icons.Outlined.Add,
                    contentDescription = null
                )
            }
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = uiState.selectedTab,
                onSelectTab = onSelectTab
            )
        }
    ) { innerPadding ->
        when (uiState.selectedTab) {
            DashboardTab.Home -> DashboardHomeContent(
                modifier = Modifier.padding(innerPadding),
                loginResult = loginResult,
                classes = classes,
                selectedDate = uiState.selectedDate,
                onTakeAttendance = onTakeAttendance
            )

            DashboardTab.Calendar -> CalendarScreen(
                modifier = Modifier.padding(innerPadding),
                isTeacher = isTeacher,
                selectedMonth = uiState.selectedMonth,
                selectedDate = uiState.selectedDate,
                weekDates = weekDates,
                classes = classes,
                onChangeMonth = onChangeMonth,
                onSelectDate = onSelectDate,
                onPrimaryAction = {
                    if (isTeacher) {
                        classes.firstOrNull()?.let { onTakeAttendance(it.id) }
                    }
                }
            )

            DashboardTab.Profile -> ProfileScreen(
                modifier = Modifier.padding(innerPadding),
                loginResult = loginResult,
                onLogout = onLogout
            )
        }
    }
}

@Composable
private fun DashboardHomeContent(
    modifier: Modifier = Modifier,
    loginResult: LoginResult,
    classes: List<ClassSchedule>,
    selectedDate: LocalDate,
    onTakeAttendance: (String) -> Unit
) {
    val isTeacher = loginResult.role == UserRole.Teacher
    val subtitle = if (isTeacher) "Your classes for today" else "Your learning plan for today"
    val progress = if (classes.isEmpty()) 0.15f else minOf(1f, classes.size / 4f)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Hello, ${loginResult.displayName.substringBefore(' ')}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$subtitle • ${selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMM"))}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText
                )
            }
        }
        item {
            HighlightStatusCard(
                role = loginResult.role,
                progress = progress,
                classCount = classes.size,
                onAction = {
                    if (isTeacher) {
                        classes.firstOrNull()?.let { onTakeAttendance(it.id) }
                    }
                }
            )
        }
        item {
            Text("Daily overview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        }
        item {
            OverviewGrid(loginResult = loginResult, classCount = classes.size)
        }
        item {
            Text(
                text = if (isTeacher) "Assigned classes" else "Today’s classes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (classes.isEmpty()) {
            item {
                EmptyStateCard(
                    title = if (isTeacher) "No classes assigned" else "No classes scheduled",
                    subtitle = "Everything is clear for this day."
                )
            }
        } else {
            items(classes) { classInfo ->
                ScheduleClassCard(
                    classInfo = classInfo,
                    isTeacher = isTeacher,
                    actionLabel = if (isTeacher) "Take Attendance" else null,
                    onAction = { if (isTeacher) onTakeAttendance(classInfo.id) }
                )
            }
        }
    }
}

@Composable
private fun HighlightStatusCard(
    role: UserRole,
    progress: Float,
    classCount: Int,
    onAction: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (role == UserRole.Teacher) "Ready for class check-in" else "You’re on track today",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (role == UserRole.Teacher) "$classCount class sessions need your review." else "$classCount classes lined up for today.",
                    color = Color(0xFFD9E8E5),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (role == UserRole.Teacher) {
                    ElevatedButton(
                        onClick = onAction,
                        shape = RoundedCornerShape(18.dp),
                        colors = androidx.compose.material3.ButtonDefaults.elevatedButtonColors(
                            containerColor = AccentAmber,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Start now")
                    }
                }
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(86.dp),
                    strokeWidth = 8.dp,
                    trackColor = Color.White.copy(alpha = 0.15f),
                    color = AccentAmber
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun OverviewGrid(
    loginResult: LoginResult,
    classCount: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OverviewMiniCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Today,
                title = "$classCount sessions",
                subtitle = "Planned today"
            )
            OverviewMiniCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Groups,
                title = if (loginResult.role == UserRole.Teacher) "20 students" else "3 teachers",
                subtitle = if (loginResult.role == UserRole.Teacher) "Shared roster" else "Faculty assigned"
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OverviewMiniCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.TaskAlt,
                title = if (loginResult.role == UserRole.Teacher) "Attendance" else "Consistency",
                subtitle = if (loginResult.role == UserRole.Teacher) "Track presence" else "Stay punctual"
            )
            OverviewMiniCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.NotificationsNone,
                title = "Gentle alerts",
                subtitle = "No clutter, just reminders"
            )
        }
    }
}

@Composable
private fun OverviewMiniCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(TealPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = TealPrimary)
            }
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = MutedText, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ScheduleClassCard(
    classInfo: ClassSchedule,
    isTeacher: Boolean,
    actionLabel: String?,
    onAction: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AccentAmber.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null, tint = AccentAmber)
                    }
                    Column {
                        Text(text = classInfo.subject, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "${classInfo.startTime} - ${classInfo.endTime}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedText
                        )
                    }
                }
                SoftBadge(icon = Icons.Outlined.Schedule, text = classInfo.room)
            }
            Text(
                text = if (isTeacher) "Teacher ID: ${classInfo.teacherId}" else "Faculty: ${AttendanceRepository.teacherName(classInfo.teacherId)}",
                color = MutedText,
                style = MaterialTheme.typography.bodyMedium
            )
            if (actionLabel != null) {
                PrimaryAccentButton(text = actionLabel, onClick = onAction)
            }
        }
    }
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    loginResult: LoginResult,
    onLogout: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AvatarCircle(name = loginResult.displayName, size = 74.dp)
                    ProfileRow(label = "Name", value = loginResult.displayName)
                    ProfileRow(label = "Role", value = loginResult.role.name)
                    ProfileRow(label = "ID", value = loginResult.userId)
                    Button(
                        onClick = onLogout,
                        shape = RoundedCornerShape(18.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = TealPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Logout")
                    }
                }
            }
        }
        item {
            EmptyStateCard(
                title = "Prepared for future updates",
                subtitle = "This structure is ready for real avatars, attendance history, Room, or Firebase later."
            )
        }
    }
}

@Composable
private fun BottomNavBar(
    selectedTab: DashboardTab,
    onSelectTab: (DashboardTab) -> Unit
) {
    val tabs = listOf(
        DashboardTab.Home to Icons.Outlined.Home,
        DashboardTab.Calendar to Icons.Outlined.CalendarMonth,
        DashboardTab.Profile to Icons.Outlined.PersonOutline
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, (tab, icon) ->
                if (index == 1) {
                    Spacer(modifier = Modifier.width(64.dp))
                }
                val active = selectedTab == tab
                val tint = if (active) TealPrimary else MutedText
                val background = if (active) TealPrimary.copy(alpha = 0.12f) else Color.Transparent
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(background)
                        .clickable { onSelectTab(tab) }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(icon, contentDescription = tab.label, tint = tint)
                    Text(tab.label, color = tint, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
