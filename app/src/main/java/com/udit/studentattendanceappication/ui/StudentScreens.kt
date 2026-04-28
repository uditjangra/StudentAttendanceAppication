package com.udit.studentattendanceappication.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FreeBreakfast
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udit.studentattendanceappication.ui.components.EmptyStateCard
import com.udit.studentattendanceappication.ui.components.SoftBadge
import com.udit.studentattendanceappication.ui.data.AttendanceRepository
import com.udit.studentattendanceappication.ui.model.ActivityCategory
import com.udit.studentattendanceappication.ui.model.ActivitySuggestion
import com.udit.studentattendanceappication.ui.model.ClassSchedule
import com.udit.studentattendanceappication.ui.model.FreePeriod
import com.udit.studentattendanceappication.ui.model.LoginResult
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
import java.time.format.DateTimeFormatter

// ═══════════════════════════════════════════════════════════════════════════════
// MY PLAN SCREEN
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun MyPlanScreen(
    modifier: Modifier = Modifier,
    loginResult: LoginResult,
    selectedDate: LocalDate,
    classes: List<ClassSchedule>,
    freePeriods: List<FreePeriod>
) {
    val isWeekend = selectedDate.dayOfWeek.value >= 6

    LazyColumn(
        modifier = modifier.fillMaxSize().background(ContentBg),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("My Plan", style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM")),
                        style = MaterialTheme.typography.bodySmall, color = TextSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(SchoolGreen.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Class ${loginResult.classSection}",
                        color = SchoolGreen, fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        if (isWeekend) {
            item { WeekendPlanCard() }
        } else {
            val timeline = buildDayTimeline(classes, freePeriods)
            if (timeline.isEmpty()) {
                item {
                    EmptyStateCard("No schedule today", "Enjoy your day or use it for self-study.")
                }
            } else {
                items(timeline) { entry ->
                    when (entry) {
                        is TimelineEntry.ClassEntry -> PlanClassCard(
                            classInfo = entry.classInfo,
                            colorIndex = classes.indexOf(entry.classInfo) % SubjectColors.size
                        )
                        is TimelineEntry.FreeEntry -> FreePeriodCard(
                            freePeriod = entry.freePeriod,
                            suggestions = entry.suggestions
                        )
                    }
                }
            }
        }
    }
}

private sealed class TimelineEntry {
    data class ClassEntry(val classInfo: ClassSchedule) : TimelineEntry()
    data class FreeEntry(val freePeriod: FreePeriod, val suggestions: List<ActivitySuggestion>) : TimelineEntry()
}

private fun buildDayTimeline(
    classes: List<ClassSchedule>,
    freePeriods: List<FreePeriod>
): List<TimelineEntry> {
    val result = mutableListOf<TimelineEntry>()
    val classMap = classes.sortedBy { it.startTime }.associateBy { it.startTime }
    val freeMap = freePeriods.sortedBy { it.startTime }.associateBy { it.startTime }
    val allTimes = (classMap.keys + freeMap.keys).toSortedSet()
    for (time in allTimes) {
        classMap[time]?.let { result.add(TimelineEntry.ClassEntry(it)) }
        freeMap[time]?.let { fp ->
            val duration = parseDurationMinutes(fp.startTime, fp.endTime)
            result.add(TimelineEntry.FreeEntry(fp, AttendanceRepository.suggestionsFor(duration)))
        }
    }
    return result
}

private fun parseDurationMinutes(start: String, end: String): Int {
    val (sh, sm) = start.split(":").map { it.toInt() }
    val (eh, em) = end.split(":").map { it.toInt() }
    return (eh * 60 + em) - (sh * 60 + sm)
}

@Composable
private fun PlanClassCard(classInfo: ClassSchedule, colorIndex: Int) {
    val cardColor = SubjectColors[colorIndex]
    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(classInfo.startTime, style = MaterialTheme.typography.labelSmall,
                    color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(classInfo.endTime, style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f))
            }
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null,
                    tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(classInfo.subject, fontWeight = FontWeight.Bold, color = Color.White)
                Text("${classInfo.room} • ${AttendanceRepository.teacherName(classInfo.teacherId)}",
                    style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            }
            Box(
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("Class", color = Color.White, style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun FreePeriodCard(freePeriod: FreePeriod, suggestions: List<ActivitySuggestion>) {
    val duration = parseDurationMinutes(freePeriod.startTime, freePeriod.endTime)
    val label = if (duration >= 60) "Lunch Break" else "Free Period"

    Card(
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(CircleShape)
                            .background(SchoolGreen.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.FreeBreakfast, contentDescription = null,
                            tint = SchoolGreen, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text(label, fontWeight = FontWeight.SemiBold, color = SchoolGreen)
                        Text("${freePeriod.startTime} – ${freePeriod.endTime} • $duration min",
                            style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }
                SoftBadge(icon = Icons.Outlined.AutoAwesome, text = "Suggested")
            }

            if (suggestions.isEmpty()) {
                Text("Take a break and recharge!",
                    style = MaterialTheme.typography.bodySmall, color = TextMuted)
            } else {
                suggestions.forEach { ActivityCard(it) }
            }
        }
    }
}

@Composable
private fun ActivityCard(suggestion: ActivitySuggestion) {
    val categoryColor = Color(suggestion.category.color)
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(categoryColor.copy(alpha = 0.08f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape)
                .background(categoryColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.AutoAwesome, contentDescription = null,
                tint = categoryColor, modifier = Modifier.size(16.dp))
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(suggestion.title, fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                Text("${suggestion.durationMinutes} min",
                    style = MaterialTheme.typography.labelSmall,
                    color = categoryColor, fontWeight = FontWeight.Medium)
            }
            Text(suggestion.description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Box(
                modifier = Modifier.clip(RoundedCornerShape(6.dp))
                    .background(categoryColor.copy(alpha = 0.12f))
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(suggestion.category.label, style = MaterialTheme.typography.labelSmall,
                    color = categoryColor, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun WeekendPlanCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = SchoolGreen),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Weekend — Your Time 🌟", fontWeight = FontWeight.Bold,
                color = Color.White, style = MaterialTheme.typography.titleMedium)
            Text("No classes today. Use this time to catch up on studies or relax.",
                color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            AttendanceRepository.suggestionsFor(60).forEach { ActivityCard(it) }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STUDENT ATTENDANCE HISTORY SCREEN
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun StudentAttendanceHistoryScreen(
    modifier: Modifier = Modifier,
    studentId: String,
    studentName: String
) {
    val attendanceMap = AttendanceRepository.simulatedStudentAttendance[studentId] ?: emptyMap()
    val totalMap = AttendanceRepository.totalClassesPerSubject
    val overallPct = if (attendanceMap.isEmpty()) 0 else attendanceMap.values.average().toInt()

    LazyColumn(
        modifier = modifier.fillMaxSize().background(ContentBg),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Attendance", style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Your attendance record this semester",
                style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }

        item { OverallAttendanceCard(overallPct) }

        item {
            Text("Subject-wise Breakdown", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        // Table header
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(CardWhite, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subject", style = MaterialTheme.typography.labelSmall,
                    color = TextMuted, modifier = Modifier.weight(1f))
                Text("Attended", style = MaterialTheme.typography.labelSmall,
                    color = TextMuted, modifier = Modifier.width(70.dp))
                Text("%", style = MaterialTheme.typography.labelSmall,
                    color = TextMuted, modifier = Modifier.width(50.dp))
            }
            Box(Modifier.fillMaxWidth().height(1.dp).background(DividerColor))
        }

        items(attendanceMap.entries.toList()) { (subject, pct) ->
            val total = totalMap[subject] ?: 20
            val attended = (total * pct / 100)
            SubjectAttendanceRow(subject = subject, percentage = pct, attended = attended, total = total)
        }

        item { AttendanceRulesCard() }
    }
}

@Composable
private fun OverallAttendanceCard(percentage: Int) {
    val color = when {
        percentage >= 85 -> AttendancePresent
        percentage >= 75 -> Color(0xFFF39C12)
        else -> AttendanceAbsent
    }
    val statusText = when {
        percentage >= 85 -> "Excellent"
        percentage >= 75 -> "Satisfactory"
        else -> "Needs Improvement"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = SchoolGreen),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Overall Attendance", color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium)
                Text("$percentage%", style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold, color = Color.White)
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(statusText, color = Color.White, fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall)
                }
            }
            Box(
                modifier = Modifier.size(72.dp).clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (percentage >= 75) Icons.Outlined.CheckCircle else Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
private fun SubjectAttendanceRow(subject: String, percentage: Int, attended: Int, total: Int) {
    val color = when {
        percentage >= 85 -> AttendancePresent
        percentage >= 75 -> Color(0xFFF39C12)
        else -> AttendanceAbsent
    }
    Column(
        modifier = Modifier.fillMaxWidth().background(CardWhite)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(subject, fontWeight = FontWeight.Medium, color = TextPrimary,
                modifier = Modifier.weight(1f))
            Text("$attended / $total", color = TextSecondary,
                style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(70.dp))
            Text("$percentage%", fontWeight = FontWeight.Bold, color = color,
                modifier = Modifier.width(50.dp))
        }
        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = color,
            trackColor = color.copy(alpha = 0.12f),
            strokeCap = StrokeCap.Round
        )
        Box(Modifier.fillMaxWidth().height(1.dp).background(DividerColor))
    }
}

@Composable
private fun AttendanceRulesCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Schedule, contentDescription = null,
                    tint = SchoolGreen, modifier = Modifier.size(18.dp))
                Text("Attendance Policy", fontWeight = FontWeight.SemiBold,
                    color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
            }
            Text("• Minimum 75% attendance required per subject",
                style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Text("• Below 75% may result in exam debarment",
                style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Text("• Medical leaves require documentation within 3 days",
                style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}
