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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.udit.studentattendanceappication.ui.model.AdminTab
import com.udit.studentattendanceappication.ui.model.AppUiState
import com.udit.studentattendanceappication.ui.model.Student
import com.udit.studentattendanceappication.ui.model.Teacher
import com.udit.studentattendanceappication.ui.theme.CardWhite
import com.udit.studentattendanceappication.ui.theme.ContentBg
import com.udit.studentattendanceappication.ui.theme.DividerColor
import com.udit.studentattendanceappication.ui.theme.SchoolGreen
import com.udit.studentattendanceappication.ui.theme.TextPrimary
import com.udit.studentattendanceappication.ui.theme.TextSecondary

// =============================================================================
// ADMIN DASHBOARD
// =============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    uiState: AppUiState,
    teachers: List<Teacher>,
    students: List<Student>,
    isLoading: Boolean,
    onSelectTab: (AdminTab) -> Unit,
    onLogout: () -> Unit,
    onAddTeacher: (String, String, String, String, String, String, String, String, String, String, (String, String) -> Unit) -> Unit,
    onAddStudent: (String, String, String, String, String, String, String, String, String, String, String, String, String, (String, String) -> Unit) -> Unit,
    onDeleteTeacher: (String) -> Unit,
    onDeleteStudent: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        containerColor = ContentBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Admin Panel", fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Smart Attendance", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = "Logout", tint = SchoolGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = CardWhite, tonalElevation = 0.dp) {
                listOf(
                    Triple("Teachers", Icons.Outlined.School,  uiState.selectedAdminTab == AdminTab.Teachers),
                    Triple("Students", Icons.Outlined.Groups,  uiState.selectedAdminTab == AdminTab.Students)
                ).forEachIndexed { index, (label, icon, selected) ->
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onSelectTab(AdminTab.entries[index]) },
                        icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(22.dp)) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SchoolGreen, selectedTextColor = SchoolGreen,
                            indicatorColor = SchoolGreen.copy(alpha = 0.12f),
                            unselectedIconColor = TextSecondary, unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (uiState.selectedAdminTab) {
                AdminTab.Teachers -> TeacherListTab(
                    teachers = teachers,
                    isLoading = isLoading,
                    onAddTeacher = onAddTeacher,
                    onDeleteTeacher = onDeleteTeacher,
                    onShowSnackbar = onShowSnackbar
                )
                AdminTab.Students -> StudentListTab(
                    students = students,
                    isLoading = isLoading,
                    onAddStudent = onAddStudent,
                    onDeleteStudent = onDeleteStudent,
                    onShowSnackbar = onShowSnackbar
                )
            }
        }
    }
}

// =============================================================================
// TEACHER LIST TAB
// =============================================================================

@Composable
private fun TeacherListTab(
    teachers: List<Teacher>,
    isLoading: Boolean,
    onAddTeacher: (String, String, String, String, String, String, String, String, String, String, (String, String) -> Unit) -> Unit,
    onDeleteTeacher: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var resultDialog by remember { mutableStateOf<Pair<String, String>?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Teachers (${teachers.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Button(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen)
                    ) {
                        Icon(Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Add Teacher")
                    }
                }
            }
            if (isLoading) {
                item { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = SchoolGreen) } }
            } else if (teachers.isEmpty()) {
                item { Text("No teachers added yet. Tap 'Add Teacher' to get started.", color = TextSecondary, style = MaterialTheme.typography.bodyMedium) }
            } else {
                items(teachers) { teacher ->
                    TeacherListCard(teacher = teacher, onDelete = { onDeleteTeacher(teacher.id) })
                }
            }
        }
    }

    if (showAddDialog) {
        AddTeacherDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, subject, age, qual, exp, phone, email, ct, subjects, password ->
                onAddTeacher(name, subject, age, qual, exp, phone, email, ct, subjects, password) { userId, pwd ->
                    showAddDialog = false
                    resultDialog = Pair(userId, pwd)
                }
            }
        )
    }

    resultDialog?.let { (userId, password) ->
        CredentialsDialog(
            userId = userId,
            password = password,
            role = "Teacher",
            onDismiss = { resultDialog = null }
        )
    }
}

@Composable
private fun TeacherListCard(teacher: Teacher, onDelete: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }
    Card(colors = CardDefaults.cardColors(containerColor = CardWhite), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(SchoolGreen), contentAlignment = Alignment.Center) {
                    Text(teacher.name.first().uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Column {
                    Text(teacher.name, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text("ID: ${teacher.id}  |  ${teacher.subject}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    if (teacher.classTeacherOf.isNotEmpty()) {
                        Text("Class Teacher: ${teacher.classTeacherOf}", style = MaterialTheme.typography.bodySmall, color = SchoolGreen)
                    }
                }
            }
            IconButton(onClick = { showConfirm = true }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = Color(0xFFE74C3C))
            }
        }
    }
    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Remove Teacher") },
            text = { Text("Remove ${teacher.name} from the system? They will no longer be able to login.") },
            confirmButton = { TextButton(onClick = { showConfirm = false; onDelete() }) { Text("Remove", color = Color(0xFFE74C3C)) } },
            dismissButton = { TextButton(onClick = { showConfirm = false }) { Text("Cancel") } }
        )
    }
}

// =============================================================================
// STUDENT LIST TAB
// =============================================================================

@Composable
private fun StudentListTab(
    students: List<Student>,
    isLoading: Boolean,
    onAddStudent: (String, String, String, String, String, String, String, String, String, String, String, String, String, (String, String) -> Unit) -> Unit,
    onDeleteStudent: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var resultDialog by remember { mutableStateOf<Pair<String, String>?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Students (${students.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Button(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen)
                    ) {
                        Icon(Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Add Student")
                    }
                }
            }
            if (isLoading) {
                item { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = SchoolGreen) } }
            } else if (students.isEmpty()) {
                item { Text("No students added yet. Tap 'Add Student' to get started.", color = TextSecondary, style = MaterialTheme.typography.bodyMedium) }
            } else {
                items(students) { student ->
                    StudentListCard(student = student, onDelete = { onDeleteStudent(student.id) })
                }
            }
        }
    }

    if (showAddDialog) {
        AddStudentDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, section, dob, gender, blood, address, pName, pPhone, pOcc, house, sports, bus, password ->
                onAddStudent(name, section, dob, gender, blood, address, pName, pPhone, pOcc, house, sports, bus, password) { userId, pwd ->
                    showAddDialog = false
                    resultDialog = Pair(userId, pwd)
                }
            }
        )
    }

    resultDialog?.let { (userId, password) ->
        CredentialsDialog(userId = userId, password = password, role = "Student", onDismiss = { resultDialog = null })
    }
}

@Composable
private fun StudentListCard(student: Student, onDelete: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }
    Card(colors = CardDefaults.cardColors(containerColor = CardWhite), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(SchoolGreen), contentAlignment = Alignment.Center) {
                    Text(student.name.first().uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Column {
                    Text(student.name, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text("ID: ${student.id}  |  Class ${student.classSection}  |  Roll ${student.rollNumber}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
            IconButton(onClick = { showConfirm = true }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = Color(0xFFE74C3C))
            }
        }
    }
    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Remove Student") },
            text = { Text("Remove ${student.name} from the system?") },
            confirmButton = { TextButton(onClick = { showConfirm = false; onDelete() }) { Text("Remove", color = Color(0xFFE74C3C)) } },
            dismissButton = { TextButton(onClick = { showConfirm = false }) { Text("Cancel") } }
        )
    }
}

// =============================================================================
// ADD TEACHER DIALOG
// =============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTeacherDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String, String, String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var qualification by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var classTeacherOf by remember { mutableStateOf("") }
    var subjectsTaught by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardWhite)) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Add New Teacher", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                Text("A User ID will be generated automatically. Set a password for this teacher.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)

                FormField("Full Name *", name) { name = it }
                FormField("Primary Subject *", subject) { subject = it }
                FormField("Set Password * (min 6 characters)", password) { password = it }
                FormField("Age", age) { age = it }
                FormField("Qualification", qualification) { qualification = it }
                FormField("Experience (e.g. 5 years)", experience) { experience = it }
                FormField("Phone", phone) { phone = it }
                FormField("School Email", email) { email = it }
                FormField("Class Teacher Of (e.g. 10-A, leave blank if none)", classTeacherOf) { classTeacherOf = it }
                FormField("Subjects Taught (comma separated)", subjectsTaught) { subjectsTaught = it }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (name.isNotBlank() && subject.isNotBlank() && password.length >= 6) {
                                onAdd(name, subject, age, qualification, experience, phone, email, classTeacherOf, subjectsTaught, password)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen)
                    ) { Text("Add Teacher") }
                }
            }
        }
    }
}

// =============================================================================
// ADD STUDENT DIALOG
// =============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddStudentDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String, String, String, String, String, String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var classSection by remember { mutableStateOf("10-A") }
    var expanded by remember { mutableStateOf(false) }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var parentName by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf("") }
    var parentOccupation by remember { mutableStateOf("") }
    var sportsHouse by remember { mutableStateOf("") }
    var sports by remember { mutableStateOf("") }
    var busRoute by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardWhite)) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Add New Student", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                Text("A User ID will be generated automatically. Set a password for this student.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)

                FormField("Full Name *", name) { name = it }
                FormField("Set Password * (min 6 characters)", password) { password = it }

                // Class section dropdown
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = classSection,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Class Section *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("10-A", "10-B").forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = { classSection = option; expanded = false })
                        }
                    }
                }

                FormField("Date of Birth (e.g. 12 Mar 2009)", dob) { dob = it }
                FormField("Gender (Male/Female)", gender) { gender = it }
                FormField("Blood Group (e.g. B+)", bloodGroup) { bloodGroup = it }
                FormField("Address", address) { address = it }
                FormField("Parent Name *", parentName) { parentName = it }
                FormField("Parent Phone", parentPhone) { parentPhone = it }
                FormField("Parent Occupation", parentOccupation) { parentOccupation = it }
                FormField("Sports House (e.g. Blue House)", sportsHouse) { sportsHouse = it }
                FormField("Sports (e.g. Cricket, Football)", sports) { sports = it }
                FormField("Bus Route (e.g. Route 3)", busRoute) { busRoute = it }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (name.isNotBlank() && parentName.isNotBlank() && password.length >= 6) {
                                onAdd(name, classSection, dob, gender, bloodGroup, address, parentName, parentPhone, parentOccupation, sportsHouse, sports, busRoute, password)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen)
                    ) { Text("Add Student") }
                }
            }
        }
    }
}

// =============================================================================
// CREDENTIALS DIALOG — shown after adding a teacher/student
// =============================================================================

@Composable
private fun CredentialsDialog(userId: String, password: String, role: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("$role Added Successfully") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Share these credentials with the $role. They can change their password after first login.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Card(colors = CardDefaults.cardColors(containerColor = SchoolGreen.copy(alpha = 0.08f)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("User ID", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text(userId, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Password", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text(password, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                        }
                    }
                }
                Text("Note down these credentials before closing.", style = MaterialTheme.typography.bodySmall, color = Color(0xFFE74C3C))
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = SchoolGreen)) {
                Text("Done")
            }
        }
    )
}

// =============================================================================
// REUSABLE FORM FIELD
// =============================================================================

@Composable
private fun FormField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = !label.contains("Address")
    )
}
