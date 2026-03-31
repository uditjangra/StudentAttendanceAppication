package com.udit.studentattendanceappication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.udit.studentattendanceappication.ui.components.AvatarCircle
import com.udit.studentattendanceappication.ui.components.EmptyStateCard
import com.udit.studentattendanceappication.ui.components.SoftBadge
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.model.ClassSchedule
import com.udit.studentattendanceappication.ui.model.Student
import com.udit.studentattendanceappication.ui.theme.AccentAmber
import com.udit.studentattendanceappication.ui.theme.AttendanceAbsent
import com.udit.studentattendanceappication.ui.theme.AttendancePresent
import com.udit.studentattendanceappication.ui.theme.MutedText
import com.udit.studentattendanceappication.ui.theme.ScreenBackground
import com.udit.studentattendanceappication.ui.theme.TealPrimary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    isTeacher: Boolean,
    selectedMonth: YearMonth,
    selectedDate: LocalDate,
    weekDates: List<LocalDate>,
    classes: List<ClassSchedule>,
    onChangeMonth: (Long) -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onPrimaryAction: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Soft calendar view",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { onChangeMonth(-1) }) {
                    Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Previous month")
                }
            },
            actions = {
                IconButton(onClick = { onChangeMonth(1) }) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowForwardIos, contentDescription = "Next month")
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                weekDates.forEach { date ->
                    DateChip(date = date, selected = date == selectedDate, onClick = { onSelectDate(date) })
                }
            }
            Button(
                onClick = onPrimaryAction,
                enabled = isTeacher && classes.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentAmber,
                    contentColor = Color.White,
                    disabledContainerColor = AccentAmber.copy(alpha = 0.35f)
                ),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text(if (isTeacher) "Take attendance for selected day" else "Student schedule view")
            }
            Text(
                text = "Schedule timeline",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            if (classes.isEmpty()) {
                EmptyStateCard(
                    title = "No sessions on this date",
                    subtitle = "Pick another day from the selector."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(classes) { classInfo ->
                        TimelineEventRow(
                            classInfo = classInfo,
                            isTeacher = isTeacher,
                            isActive = classInfo == classes.first()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DateChip(
    date: LocalDate,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (selected) TealPrimary else MaterialTheme.colorScheme.surface
    val contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 8.dp else 3.dp)
    ) {
        Column(
            modifier = Modifier
                .width(70.dp)
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                color = contentColor.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.dayOfMonth.toString(),
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TimelineEventRow(
    classInfo: ClassSchedule,
    isTeacher: Boolean,
    isActive: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = classInfo.startTime, style = MaterialTheme.typography.bodySmall, color = MutedText)
            Canvas(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(width = 16.dp, height = 96.dp)
            ) {
                drawCircle(
                    color = if (isActive) AccentAmber else TealPrimary.copy(alpha = 0.35f),
                    radius = 8f,
                    center = Offset(size.width / 2, 12f)
                )
                drawLine(
                    color = TealPrimary.copy(alpha = 0.22f),
                    start = Offset(size.width / 2, 24f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 4f
                )
            }
        }
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = if (isActive) Color(0xFFFFF3E7) else MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = classInfo.subject, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "${classInfo.startTime} - ${classInfo.endTime} • ${classInfo.room}",
                    color = MutedText,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (isTeacher) "Teacher ID ${classInfo.teacherId}" else AttendanceRepository.teacherName(classInfo.teacherId),
                    color = if (isActive) AccentAmber else MutedText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    classInfo: ClassSchedule,
    attendance: Map<String, Boolean>,
    onToggleAttendance: (String, Boolean) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(classInfo.subject, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${classInfo.startTime} - ${classInfo.endTime}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedText
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Attendance roster", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "${AttendanceRepository.students.size} students • ${classInfo.room}",
                                color = MutedText,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        SoftBadge(icon = Icons.Outlined.CheckCircle, text = "Live toggle")
                    }
                }
            }
            items(AttendanceRepository.students) { student ->
                StudentAttendanceCard(
                    student = student,
                    present = attendance[student.id] == true,
                    onToggle = { onToggleAttendance(student.id, it) }
                )
            }
        }
    }
}

@Composable
private fun StudentAttendanceCard(
    student: Student,
    present: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val accentColor = if (present) AttendancePresent else AttendanceAbsent
    Card(
        colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.18f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = student.serialNumber.toString().padStart(2, '0'),
                    color = MutedText,
                    modifier = Modifier.width(28.dp)
                )
                AvatarCircle(name = student.name, size = 48.dp)
                Column {
                    Text(student.name, fontWeight = FontWeight.SemiBold)
                    Text(student.id, color = MutedText, style = MaterialTheme.typography.bodySmall)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (present) "Present" else "Absent",
                    color = accentColor,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(checked = present, onCheckedChange = onToggle)
            }
        }
    }
}
