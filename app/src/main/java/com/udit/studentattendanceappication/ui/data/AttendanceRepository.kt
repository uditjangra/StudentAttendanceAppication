package com.udit.studentattendanceappication.ui.data

import com.udit.studentattendanceappication.ui.model.ClassSchedule
import com.udit.studentattendanceappication.ui.model.Student
import com.udit.studentattendanceappication.ui.model.Teacher
import java.time.DayOfWeek

object AttendanceRepository {
    val teachers = listOf(
        Teacher("10000", "Aarav Mehta"),
        Teacher("10001", "Neha Sharma"),
        Teacher("10002", "Rohan Iyer")
    )

    val students = listOf(
        "Aisha Kapoor", "Vivaan Singh", "Anika Rao", "Kabir Nair", "Meera Patel",
        "Arjun Verma", "Ishita Sen", "Reyansh Khanna", "Diya Menon", "Advait Joshi",
        "Sara Bansal", "Krish Malhotra", "Myra Desai", "Yash Tiwari", "Kiara Sethi",
        "Atharv Kulkarni", "Riya Chawla", "Vihaan Arora", "Naina Dutta", "Parth Gill"
    ).mapIndexed { index, name ->
        Student(
            serialNumber = index + 1,
            id = (12301 + index).toString(),
            name = name
        )
    }

    val schedule = listOf(
        ClassSchedule("MON-1", "Mathematics", DayOfWeek.MONDAY, "09:00", "10:00", "10000", "Room 201"),
        ClassSchedule("MON-2", "Physics", DayOfWeek.MONDAY, "10:20", "11:20", "10001", "Room 106"),
        ClassSchedule("MON-3", "English", DayOfWeek.MONDAY, "11:40", "12:30", "10002", "Room 302"),
        ClassSchedule("MON-4", "Computer Science", DayOfWeek.MONDAY, "13:30", "14:30", "10000", "Lab 2"),
        ClassSchedule("TUE-1", "Chemistry", DayOfWeek.TUESDAY, "09:00", "10:00", "10001", "Room 204"),
        ClassSchedule("TUE-2", "Mathematics", DayOfWeek.TUESDAY, "10:20", "11:20", "10000", "Room 201"),
        ClassSchedule("TUE-3", "Computer Science", DayOfWeek.TUESDAY, "11:40", "12:30", "10002", "Lab 1"),
        ClassSchedule("TUE-4", "English", DayOfWeek.TUESDAY, "13:30", "14:20", "10001", "Room 302"),
        ClassSchedule("WED-1", "Physics", DayOfWeek.WEDNESDAY, "09:00", "10:00", "10001", "Room 106"),
        ClassSchedule("WED-2", "Chemistry", DayOfWeek.WEDNESDAY, "10:20", "11:20", "10002", "Room 204"),
        ClassSchedule("WED-3", "Mathematics", DayOfWeek.WEDNESDAY, "11:40", "12:30", "10000", "Room 201"),
        ClassSchedule("WED-4", "Computer Science", DayOfWeek.WEDNESDAY, "13:30", "14:30", "10002", "Lab 2"),
        ClassSchedule("THU-1", "English", DayOfWeek.THURSDAY, "09:00", "10:00", "10001", "Room 302"),
        ClassSchedule("THU-2", "Mathematics", DayOfWeek.THURSDAY, "10:20", "11:20", "10000", "Room 201"),
        ClassSchedule("THU-3", "Physics", DayOfWeek.THURSDAY, "11:40", "12:30", "10002", "Room 106"),
        ClassSchedule("THU-4", "Chemistry", DayOfWeek.THURSDAY, "13:30", "14:20", "10001", "Room 204"),
        ClassSchedule("FRI-1", "Computer Science", DayOfWeek.FRIDAY, "09:00", "10:00", "10002", "Lab 1"),
        ClassSchedule("FRI-2", "English", DayOfWeek.FRIDAY, "10:20", "11:20", "10001", "Room 302"),
        ClassSchedule("FRI-3", "Mathematics", DayOfWeek.FRIDAY, "11:40", "12:30", "10000", "Room 201"),
        ClassSchedule("FRI-4", "Physics", DayOfWeek.FRIDAY, "13:30", "14:30", "10002", "Room 106")
    )

    fun teacherName(id: String): String = teachers.firstOrNull { it.id == id }?.name ?: "Faculty"
    fun studentName(id: String): String = students.firstOrNull { it.id == id }?.name ?: "Student"
}
