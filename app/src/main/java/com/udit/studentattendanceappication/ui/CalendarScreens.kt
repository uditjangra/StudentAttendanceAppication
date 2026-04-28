package com.udit.studentattendanceappication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DoneAll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udit.studentattendanceappication.ui.components.AvatarCircle
import com.udit.studentattendanceappication.ui.components.EmptyStateCard
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

// ── Teacher Calendar Screen ───────────────────────────────────────────────────
// Shows a vertical month grid. Only today and past dates are clickable.
// Clicking a past date shows the student attendance list for that day.
// Clicking today opens the live attendance marking screen.

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
    onTakeAttendance: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val today = LocalDate.now()

    Column(modifier = modifier.fillMaxSize().background(ContentBg)) {
        // Month header
        Row(
            modifier = Modifier.fillMaxWidth().background(CardWhite).padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                Text(selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMM")), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Row {
                IconButton(onClick = { onChangeMonth(-1) }) { Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Prev", tint = SchoolGreen, modifier = Modifier.size(18.dp)) }
                IconButton(onClick = { onChangeMonth(1) }) { Icon(Icons.AutoMirrored.Outlined.ArrowForwardIos, contentDescription = "Next", tint = SchoolGreen, modifier = Modifier.size(18.dp)) }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // Day-of-week header row
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun").forEach { day ->
                        Text(day, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Calendar grid — vertical rows of weeks
            val firstDay = selectedMonth.atDay(1)
            val startOffset = (firstDay.dayOfWeek.value - 1) // Mon=0
            val daysInMonth = selectedMonth.lengthOfMonth()
            val totalCells = startOffset + daysInMonth
            val weeks = (0 until ((totalCells + 6) / 7)).toList()

            items(weeks) { weekIndex ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    (0..6).forEach { dayOfWeek ->
                        val cellIndex = weekIndex * 7 + dayOfWeek
                        val dayNumber = cellIndex - startOffset + 1
                        if (dayNumber < 1 || dayNumber > daysInMonth) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val date = selectedMonth.atDay(dayNumber)
                            val isPast = date.isBefore(today)
                            val isToday = date == today
                            val isSelected = date == selectedDate
                            val isClickable = isPast || isToday
                            val isWeekend = date.dayOfWeek.value >= 6

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> SchoolGreen
                                            isToday -> SchoolGreen.copy(alpha = 0.15f)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .then(if (isClickable) Modifier.clickable { onSelectDate(date) } else Modifier)
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    color = when {
                                        isSelected -> Color.White
                                        isToday -> SchoolGreen
                                        isWeekend -> TextSecondary.copy(alpha = 0.5f)
                                        !isClickable -> TextSecondary.copy(alpha = 0.3f)
                                        else -> TextPrimary
                                    },
                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            // Divider
            item { Box(Modifier.fillMaxWidth().height(1.dp).background(DividerColor)) }

            // Selected date info
            item {
                val isPastDate = selectedDate.isBefore(today)
                val isTodaySelected = selectedDate == today
                val isFuture = selectedDate.isAfter(today)

                when {
                    isFuture -> {
                        EmptyStateCard("Future date", "Select today or a past date to view attendance.")
                    }
                    isTodaySelected -> {
                        // Today: show classes with take attendance buttons
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Today's Classes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                            if (classes.isEmpty()) {
                                EmptyStateCard("No classes today", "Nothing scheduled.")
                            } else {
                                classes.forEach { cls ->
                                    CalendarClassCard(cls, isTeacher, onTakeAttendance, onShowSnackbar)
                                }
                            }
                        }
                    }
                    isPastDate -> {
                        // Past date: show simulated attendance summary
                        val pastClasses = AttendanceRepository.schedule.filter {
                            it.dayOfWeek == selectedDate.dayOfWeek && it.teacherId == "10000"
                        }.sortedBy { it.periodNumber }
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                "Attendance — ${selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))}",
                                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary
                            )
                            if (pastClasses.isEmpty()) {
                                EmptyStateCard("No classes on this day", "No records found.")
                            } else {
                                pastClasses.forEach { cls ->
                                    PastDateAttendanceCard(cls, selectedDate)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarClassCard(
    classInfo: ClassSchedule,
    isTeacher: Boolean,
    onTakeAttendance: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val colorIndex = AttendanceRepository.schedule.indexOf(classInfo) % SubjectColors.size
    val cardColor = SubjectColors[colorIndex]
    Card(colors = CardDefaults.cardColors(containerColor = cardColor), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(3.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(classInfo.subject, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleSmall)
                    Text("Period ${classInfo.periodNumber}  •  ${classInfo.startTime}–${classInfo.endTime}  •  ${classInfo.room}", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                    Text("Class ${classInfo.classSection}", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
                }
            }
            if (isTeacher) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(Color.White.copy(alpha = 0.2f)).clickable { onTakeAttendance(classInfo.id) }.padding(vertical = 9.dp), contentAlignment = Alignment.Center) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Text("Mark Attendance", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                    Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(Color.White.copy(alpha = 0.15f)).clickable { onShowSnackbar("Timetable saved for ${classInfo.subject}") }.padding(vertical = 9.dp), contentAlignment = Alignment.Center) {
                        Text("View Timetable", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun PastDateAttendanceCard(classInfo: ClassSchedule, date: LocalDate) {
    // Simulate past attendance: use date hash for deterministic but varied data
    val seed = date.dayOfMonth + classInfo.periodNumber
    val sectionStudents = AttendanceRepository.studentsInSection(classInfo.classSection)
    val presentCount = (sectionStudents.size * (6 + (seed % 4)) / 10).coerceIn(0, sectionStudents.size)
    val absentCount = sectionStudents.size - presentCount

    Card(colors = CardDefaults.cardColors(containerColor = CardWhite), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp), border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(classInfo.subject, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Period ${classInfo.periodNumber}  •  ${classInfo.startTime}–${classInfo.endTime}  •  Class ${classInfo.classSection}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(AttendancePresent.copy(alpha = 0.12f)).padding(horizontal = 10.dp, vertical = 5.dp)) {
                        Text("P: $presentCount", color = AttendancePresent, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(AttendanceAbsent.copy(alpha = 0.12f)).padding(horizontal = 10.dp, vertical = 5.dp)) {
                        Text("A: $absentCount", color = AttendanceAbsent, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            // Student list
            Box(Modifier.fillMaxWidth().height(1.dp).background(DividerColor))
            sectionStudents.forEachIndexed { index, student ->
                val isPresent = index < presentCount
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(student.serialNumber.toString().padStart(2,'0'), color = TextMuted, style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(24.dp))
                        AvatarCircle(name = student.name, size = 30.dp)
                        Text(student.name, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.Medium)
                    }
                    Box(modifier = Modifier.clip(CircleShape).background(if (isPresent) AttendancePresent.copy(alpha = 0.15f) else AttendanceAbsent.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 3.dp)) {
                        Text(if (isPresent) "P" else "A", color = if (isPresent) AttendancePresent else AttendanceAbsent, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

// ── Teacher Attendance Screen (live marking) ──────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    classInfo: ClassSchedule,
    date: LocalDate,
    sectionStudents: List<Student>,
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
                        Text("Class ${classInfo.classSection}  •  Period ${classInfo.periodNumber}  •  ${date.format(DateTimeFormatter.ofPattern("d MMM"))}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Back", tint = SchoolGreen, modifier = Modifier.size(20.dp)) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { AttendanceSummaryCard(presentCount, absentCount, sectionStudents.size, onMarkAllPresent) }
            item {
                Row(modifier = Modifier.fillMaxWidth().background(CardWhite, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)).padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("No.", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.width(32.dp))
                    Text("Name", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.weight(1f))
                    Text("ID", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.width(60.dp))
                    Text("Status", style = MaterialTheme.typography.labelSmall, color = TextMuted, modifier = Modifier.width(80.dp))
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(DividerColor))
            }
            items(sectionStudents) { student ->
                StudentAttendanceRow(student = student, present = attendance[student.id] == true, onToggle = { onToggleAttendance(student.id, it) })
            }
        }
    }
}

@Composable
private fun AttendanceSummaryCard(presentCount: Int, absentCount: Int, totalStudents: Int, onMarkAllPresent: () -> Unit) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SchoolGreen), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Live Attendance", fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color.White.copy(alpha = 0.2f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                    Text("$totalStudents students", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.18f)).padding(14.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("$presentCount", fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color.White)
                        Text("Present", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                    }
                }
                Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.18f)).padding(14.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("$absentCount", fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color.White)
                        Text("Absent", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            OutlinedButton(onClick = onMarkAllPresent, modifier = Modifier.fillMaxWidth().height(44.dp), shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White.copy(alpha = 0.6f))) {
                Icon(Icons.Outlined.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Mark All Present", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun StudentAttendanceRow(student: Student, present: Boolean, onToggle: (Boolean) -> Unit) {
    val statusColor = if (present) AttendancePresent else AttendanceAbsent
    Row(modifier = Modifier.fillMaxWidth().background(CardWhite).padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(student.serialNumber.toString().padStart(2,'0'), color = TextMuted, style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(32.dp))
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            AvatarCircle(name = student.name, size = 36.dp)
            Text(student.name, fontWeight = FontWeight.Medium, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
        }
        Text(student.id, color = TextSecondary, style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(60.dp))
        Row(modifier = Modifier.width(80.dp), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
            Text(if (present) "Present" else "Absent", color = statusColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
        }
        Switch(checked = present, onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SchoolGreen, uncheckedThumbColor = Color.White, uncheckedTrackColor = AttendanceAbsent.copy(alpha = 0.5f)))
    }
    Box(Modifier.fillMaxWidth().height(1.dp).background(DividerColor))
}
