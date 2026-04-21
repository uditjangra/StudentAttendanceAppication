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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udit.studentattendanceappication.ui.components.AvatarCircle
import com.udit.studentattendanceappication.ui.components.EmptyStateCard
import com.udit.studentattendanceappication.ui.components.SoftBadge
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.model.ClassSchedule
import com.udit.studentattendanceappication.ui.model.Student
import com.udit.studentattendanceappication.ui.theme.AttendanceAbsent
import com.udit.studentattendanceappication.ui.theme.AttendancePresent
import com.udit.studentattendanceappication.ui.theme.CardWhite
import com.udit.studentattendanceappication.ui.theme.ContentBg
import com.udit.studentattendanceappication.ui.theme.DividerColor
import com.udit.studentattendanceappication.ui.theme.SchoolGreen
import com.udit.studentattendanceappication.ui.theme.SubjectColors
import com.udit.studentattendanceappication.ui.theme.TextMuted
import com.udit.studentattendanceappication.ui.theme.TextPrimary
import com.udit.studentattendanceappication.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

// ─── Teacher Calendar Screen ──────────────────────────────────────────────────

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
            .background(ContentBg)
    ) {
        // Header bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardWhite)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMM")),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Row {
                IconButton(onClick = { onChangeMonth(-1) }) {
                    Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Prev",
                        tint = SchoolGreen, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = { onChangeMonth(1) }) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowForwardIos, contentDescription = "Next",
                        tint = SchoolGreen, modifier = Modifier.size(18.dp))
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Week date chips
            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    weekDates.forEach { date ->
                        DateChip(date = date, selected = date == selectedDate,
                            onClick = { onSelectDate(date) })
                    }
                }
            }

            // Take attendance button (teacher only)
            if (isTeacher) {
                item {
                    Button(
                        onClick = onPrimaryAction,
                        enabled = classes.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SchoolGreen,
                            contentColor = Color.White,
                            disabledContainerColor = SchoolGreen.copy(alpha = 0.35f)
                        )
                    ) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null,
                            modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Take Attendance — ${selectedDate.format(DateTimeFormatter.ofPattern("d MMM"))}",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            item {
                Text("Schedule Timeline", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            if (classes.isEmpty()) {
                item {
                    EmptyStateCard(
                        title = "No sessions on this date",
                        subtitle = "Pick another day from the selector above."
                    )
                }
            } else {
                items(classes.size) { index ->
                    TimelineEventRow(
                        classInfo = classes[index],
                        isTeacher = isTeacher,
                        isActive = index == 0,
                        colorIndex = index % SubjectColors.size
                    )
                }
            }
        }
    }
}

@Composable
internal fun DateChip(date: LocalDate, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) SchoolGreen else CardWhite
    val textColor = if (selected) Color.White else TextPrimary
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = bg),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 6.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.width(64.dp).padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                color = textColor.copy(alpha = 0.75f),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun TimelineEventRow(
    classInfo: ClassSchedule,
    isTeacher: Boolean,
    isActive: Boolean,
    colorIndex: Int
) {
    val accentColor = SubjectColors[colorIndex]
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Timeline line + dot
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(classInfo.startTime, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Canvas(modifier = Modifier.padding(top = 4.dp).size(width = 14.dp, height = 88.dp)) {
                drawCircle(
                    color = if (isActive) accentColor else accentColor.copy(alpha = 0.4f),
                    radius = 7f,
                    center = Offset(size.width / 2, 10f)
                )
                drawLine(
                    color = accentColor.copy(alpha = 0.2f),
                    start = Offset(size.width / 2, 22f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 3f
                )
            }
        }
        // Event card
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = if (isActive) accentColor.copy(alpha = 0.08f) else CardWhite
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 4.dp else 1.dp),
            border = if (isActive) androidx.compose.foundation.BorderStroke(1.5.dp, accentColor.copy(alpha = 0.3f)) else null
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(classInfo.subject, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Text(
                    "${classInfo.startTime} – ${classInfo.endTime} • ${classInfo.room}",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    if (isTeacher) "Teacher: ${AttendanceRepository.teacherName(classInfo.teacherId)}"
                    else AttendanceRepository.teacherName(classInfo.teacherId),
                    color = if (isActive) accentColor else TextMuted,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal
                )
            }
        }
    }
}

// ─── Teacher Attendance Screen ────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    classInfo: ClassSchedule,
    date: LocalDate,
    sectionStudents: List<com.udit.studentattendanceappication.ui.model.Student>,
    attendance: Map<String, Boolean>,
    presentCount: Int,
    absentCount: Int,
    onToggleAttendance: (String, Boolean) -> Unit,
    onMarkAllPresent: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = ContentBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(classInfo.subject, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            "Class ${classInfo.classSection}  •  Period ${classInfo.periodNumber}  •  ${date.format(DateTimeFormatter.ofPattern("d MMM"))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Back",
                            tint = SchoolGreen, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { AttendanceSummaryCard(presentCount, absentCount, sectionStudents.size, classInfo.room, onMarkAllPresent) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardWhite, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("No.", style = MaterialTheme.typography.labelSmall,
                        color = TextMuted, modifier = Modifier.width(32.dp))
                    Text("Name", style = MaterialTheme.typography.labelSmall,
                        color = TextMuted, modifier = Modifier.weight(1f))
                    Text("ID", style = MaterialTheme.typography.labelSmall,
                        color = TextMuted, modifier = Modifier.width(60.dp))
                    Text("Status", style = MaterialTheme.typography.labelSmall,
                        color = TextMuted, modifier = Modifier.width(80.dp))
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(DividerColor))
            }

            items(sectionStudents) { student ->
                StudentAttendanceRow(
                    student = student,
                    present = attendance[student.id] == true,
                    onToggle = { onToggleAttendance(student.id, it) }
                )
            }
        }
    }
}

@Composable
private fun AttendanceSummaryCard(
    presentCount: Int,
    absentCount: Int,
    totalStudents: Int,
    room: String,
    onMarkAllPresent: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SchoolGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Live Attendance", fontWeight = FontWeight.Bold,
                    color = Color.White, style = MaterialTheme.typography.titleMedium)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("${totalStudents} students",
                        color = Color.White, style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier.weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("$presentCount", fontWeight = FontWeight.Bold,
                            fontSize = 28.sp, color = Color.White)
                        Text("Present", color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall)
                    }
                }
                Box(
                    modifier = Modifier.weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("$absentCount", fontWeight = FontWeight.Bold,
                            fontSize = 28.sp, color = Color.White)
                        Text("Absent", color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            OutlinedButton(
                onClick = onMarkAllPresent,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White.copy(alpha = 0.6f))
            ) {
                Icon(Icons.Outlined.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Mark All Present", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun StudentAttendanceRow(
    student: Student,
    present: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val statusColor = if (present) AttendancePresent else AttendanceAbsent
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardWhite)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = student.serialNumber.toString().padStart(2, '0'),
            color = TextMuted,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(32.dp)
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarCircle(name = student.name, size = 36.dp)
            Text(student.name, fontWeight = FontWeight.Medium,
                color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
        }
        Text(student.id, color = TextSecondary,
            style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(60.dp))
        Row(
            modifier = Modifier.width(80.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(statusColor)
            )
            Text(
                text = if (present) "Present" else "Absent",
                color = statusColor,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
        Switch(
            checked = present,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SchoolGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = AttendanceAbsent.copy(alpha = 0.5f)
            )
        )
    }
    Box(Modifier.fillMaxWidth().height(1.dp).background(DividerColor))
}
