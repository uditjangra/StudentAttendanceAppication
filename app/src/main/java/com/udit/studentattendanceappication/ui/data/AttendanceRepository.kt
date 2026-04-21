package com.udit.studentattendanceappication.ui.data

import com.udit.studentattendanceappication.ui.model.ActivityCategory
import com.udit.studentattendanceappication.ui.model.ActivitySuggestion
import com.udit.studentattendanceappication.ui.model.ClassSchedule
import com.udit.studentattendanceappication.ui.model.FreePeriod
import com.udit.studentattendanceappication.ui.model.Student
import com.udit.studentattendanceappication.ui.model.StudentGoal
import com.udit.studentattendanceappication.ui.model.Teacher
import java.time.DayOfWeek

object AttendanceRepository {

    // ── Teachers ──────────────────────────────────────────────────────────────
    // Each teacher has a primary subject they teach across sections
    val teachers = listOf(
        Teacher("10000", "Aarav Mehta",   "Mathematics"),
        Teacher("10001", "Neha Sharma",   "Science"),
        Teacher("10002", "Rohan Iyer",    "English"),
        Teacher("10003", "Priya Nair",    "Social Studies"),
        Teacher("10004", "Suresh Pillai", "Computer Science")
    )

    // ── Students ──────────────────────────────────────────────────────────────
    // 20 students split across two sections: 10-A (12301–12310) and 10-B (12311–12320)
    val students: List<Student> = listOf(
        // Class 10-A
        Student(1,  "12301", "Aisha Kapoor",    "10-A"),
        Student(2,  "12302", "Vivaan Singh",    "10-A"),
        Student(3,  "12303", "Anika Rao",       "10-A"),
        Student(4,  "12304", "Kabir Nair",      "10-A"),
        Student(5,  "12305", "Meera Patel",     "10-A"),
        Student(6,  "12306", "Arjun Verma",     "10-A"),
        Student(7,  "12307", "Ishita Sen",      "10-A"),
        Student(8,  "12308", "Reyansh Khanna",  "10-A"),
        Student(9,  "12309", "Diya Menon",      "10-A"),
        Student(10, "12310", "Advait Joshi",    "10-A"),
        // Class 10-B
        Student(1,  "12311", "Sara Bansal",     "10-B"),
        Student(2,  "12312", "Krish Malhotra",  "10-B"),
        Student(3,  "12313", "Myra Desai",      "10-B"),
        Student(4,  "12314", "Yash Tiwari",     "10-B"),
        Student(5,  "12315", "Kiara Sethi",     "10-B"),
        Student(6,  "12316", "Atharv Kulkarni", "10-B"),
        Student(7,  "12317", "Riya Chawla",     "10-B"),
        Student(8,  "12318", "Vihaan Arora",    "10-B"),
        Student(9,  "12319", "Naina Dutta",     "10-B"),
        Student(10, "12320", "Parth Gill",      "10-B")
    )

    // ── School Timetable ──────────────────────────────────────────────────────
    // Periods: 1→09:00-09:45  2→09:55-10:40  3→10:50-11:35
    //          LUNCH 11:35-12:15
    //          4→12:15-13:00  5→13:10-13:55  6→14:05-14:50
    //
    // Subjects taught (proper school set):
    //   Mathematics, Physics, Chemistry, Biology,
    //   English Language, English Literature,
    //   History & Civics, Geography,
    //   Computer Science, Physical Education
    //
    // Each teacher teaches their subject to BOTH sections on different days/periods
    // Teacher 10000 → Mathematics
    // Teacher 10001 → Physics & Chemistry (Science dept)
    // Teacher 10002 → English Language & Literature
    // Teacher 10003 → History & Civics, Geography (Social Studies)
    // Teacher 10004 → Computer Science, Physical Education

    val schedule: List<ClassSchedule> = listOf(

        // ── MONDAY ────────────────────────────────────────────────────────────
        ClassSchedule("MON-A1", "Mathematics",        "10-A", DayOfWeek.MONDAY, "09:00","09:45", "10000","Room 101", 1),
        ClassSchedule("MON-A2", "Physics",             "10-A", DayOfWeek.MONDAY, "09:55","10:40", "10001","Lab 1",    2),
        ClassSchedule("MON-A3", "English Language",    "10-A", DayOfWeek.MONDAY, "10:50","11:35", "10002","Room 103", 3),
        ClassSchedule("MON-A4", "History & Civics",    "10-A", DayOfWeek.MONDAY, "12:15","13:00", "10003","Room 104", 4),
        ClassSchedule("MON-A5", "Computer Science",    "10-A", DayOfWeek.MONDAY, "13:10","13:55", "10004","Lab 2",    5),
        ClassSchedule("MON-A6", "Geography",           "10-A", DayOfWeek.MONDAY, "14:05","14:50", "10003","Room 104", 6),

        ClassSchedule("MON-B1", "English Language",    "10-B", DayOfWeek.MONDAY, "09:00","09:45", "10002","Room 103", 1),
        ClassSchedule("MON-B2", "Mathematics",         "10-B", DayOfWeek.MONDAY, "09:55","10:40", "10000","Room 101", 2),
        ClassSchedule("MON-B3", "Chemistry",           "10-B", DayOfWeek.MONDAY, "10:50","11:35", "10001","Lab 1",    3),
        ClassSchedule("MON-B4", "Computer Science",    "10-B", DayOfWeek.MONDAY, "12:15","13:00", "10004","Lab 2",    4),
        ClassSchedule("MON-B5", "History & Civics",    "10-B", DayOfWeek.MONDAY, "13:10","13:55", "10003","Room 104", 5),
        ClassSchedule("MON-B6", "Physical Education",  "10-B", DayOfWeek.MONDAY, "14:05","14:50", "10004","Ground",   6),

        // ── TUESDAY ───────────────────────────────────────────────────────────
        ClassSchedule("TUE-A1", "Chemistry",           "10-A", DayOfWeek.TUESDAY,"09:00","09:45", "10001","Lab 1",    1),
        ClassSchedule("TUE-A2", "Mathematics",         "10-A", DayOfWeek.TUESDAY,"09:55","10:40", "10000","Room 101", 2),
        ClassSchedule("TUE-A3", "Biology",             "10-A", DayOfWeek.TUESDAY,"10:50","11:35", "10001","Lab 3",    3),
        ClassSchedule("TUE-A4", "English Literature",  "10-A", DayOfWeek.TUESDAY,"12:15","13:00", "10002","Room 103", 4),
        ClassSchedule("TUE-A5", "Geography",           "10-A", DayOfWeek.TUESDAY,"13:10","13:55", "10003","Room 104", 5),
        ClassSchedule("TUE-A6", "Physical Education",  "10-A", DayOfWeek.TUESDAY,"14:05","14:50", "10004","Ground",   6),

        ClassSchedule("TUE-B1", "Physics",             "10-B", DayOfWeek.TUESDAY,"09:00","09:45", "10001","Lab 1",    1),
        ClassSchedule("TUE-B2", "English Literature",  "10-B", DayOfWeek.TUESDAY,"09:55","10:40", "10002","Room 103", 2),
        ClassSchedule("TUE-B3", "Mathematics",         "10-B", DayOfWeek.TUESDAY,"10:50","11:35", "10000","Room 101", 3),
        ClassSchedule("TUE-B4", "Biology",             "10-B", DayOfWeek.TUESDAY,"12:15","13:00", "10001","Lab 3",    4),
        ClassSchedule("TUE-B5", "Computer Science",    "10-B", DayOfWeek.TUESDAY,"13:10","13:55", "10004","Lab 2",    5),
        ClassSchedule("TUE-B6", "Geography",           "10-B", DayOfWeek.TUESDAY,"14:05","14:50", "10003","Room 104", 6),

        // ── WEDNESDAY ─────────────────────────────────────────────────────────
        ClassSchedule("WED-A1", "Physics",             "10-A", DayOfWeek.WEDNESDAY,"09:00","09:45","10001","Lab 1",   1),
        ClassSchedule("WED-A2", "English Literature",  "10-A", DayOfWeek.WEDNESDAY,"09:55","10:40","10002","Room 103",2),
        ClassSchedule("WED-A3", "Mathematics",         "10-A", DayOfWeek.WEDNESDAY,"10:50","11:35","10000","Room 101",3),
        ClassSchedule("WED-A4", "Biology",             "10-A", DayOfWeek.WEDNESDAY,"12:15","13:00","10001","Lab 3",   4),
        ClassSchedule("WED-A5", "Computer Science",    "10-A", DayOfWeek.WEDNESDAY,"13:10","13:55","10004","Lab 2",   5),
        ClassSchedule("WED-A6", "Physical Education",  "10-A", DayOfWeek.WEDNESDAY,"14:05","14:50","10004","Ground",  6),

        ClassSchedule("WED-B1", "Mathematics",         "10-B", DayOfWeek.WEDNESDAY,"09:00","09:45","10000","Room 101",1),
        ClassSchedule("WED-B2", "Chemistry",           "10-B", DayOfWeek.WEDNESDAY,"09:55","10:40","10001","Lab 1",   2),
        ClassSchedule("WED-B3", "English Language",    "10-B", DayOfWeek.WEDNESDAY,"10:50","11:35","10002","Room 103",3),
        ClassSchedule("WED-B4", "History & Civics",    "10-B", DayOfWeek.WEDNESDAY,"12:15","13:00","10003","Room 104",4),
        ClassSchedule("WED-B5", "Biology",             "10-B", DayOfWeek.WEDNESDAY,"13:10","13:55","10001","Lab 3",   5),
        ClassSchedule("WED-B6", "Geography",           "10-B", DayOfWeek.WEDNESDAY,"14:05","14:50","10003","Room 104",6),

        // ── THURSDAY ──────────────────────────────────────────────────────────
        ClassSchedule("THU-A1", "English Language",    "10-A", DayOfWeek.THURSDAY,"09:00","09:45","10002","Room 103", 1),
        ClassSchedule("THU-A2", "History & Civics",    "10-A", DayOfWeek.THURSDAY,"09:55","10:40","10003","Room 104", 2),
        ClassSchedule("THU-A3", "Chemistry",           "10-A", DayOfWeek.THURSDAY,"10:50","11:35","10001","Lab 1",    3),
        ClassSchedule("THU-A4", "Mathematics",         "10-A", DayOfWeek.THURSDAY,"12:15","13:00","10000","Room 101", 4),
        ClassSchedule("THU-A5", "Biology",             "10-A", DayOfWeek.THURSDAY,"13:10","13:55","10001","Lab 3",    5),
        ClassSchedule("THU-A6", "Computer Science",    "10-A", DayOfWeek.THURSDAY,"14:05","14:50","10004","Lab 2",    6),

        ClassSchedule("THU-B1", "Chemistry",           "10-B", DayOfWeek.THURSDAY,"09:00","09:45","10001","Lab 1",    1),
        ClassSchedule("THU-B2", "Mathematics",         "10-B", DayOfWeek.THURSDAY,"09:55","10:40","10000","Room 101", 2),
        ClassSchedule("THU-B3", "English Language",    "10-B", DayOfWeek.THURSDAY,"10:50","11:35","10002","Room 103", 3),
        ClassSchedule("THU-B4", "Geography",           "10-B", DayOfWeek.THURSDAY,"12:15","13:00","10003","Room 104", 4),
        ClassSchedule("THU-B5", "Physical Education",  "10-B", DayOfWeek.THURSDAY,"13:10","13:55","10004","Ground",   5),
        ClassSchedule("THU-B6", "Computer Science",    "10-B", DayOfWeek.THURSDAY,"14:05","14:50","10004","Lab 2",    6),

        // ── FRIDAY ────────────────────────────────────────────────────────────
        ClassSchedule("FRI-A1", "Computer Science",    "10-A", DayOfWeek.FRIDAY,"09:00","09:45","10004","Lab 2",      1),
        ClassSchedule("FRI-A2", "Biology",             "10-A", DayOfWeek.FRIDAY,"09:55","10:40","10001","Lab 3",      2),
        ClassSchedule("FRI-A3", "Geography",           "10-A", DayOfWeek.FRIDAY,"10:50","11:35","10003","Room 104",   3),
        ClassSchedule("FRI-A4", "Mathematics",         "10-A", DayOfWeek.FRIDAY,"12:15","13:00","10000","Room 101",   4),
        ClassSchedule("FRI-A5", "English Language",    "10-A", DayOfWeek.FRIDAY,"13:10","13:55","10002","Room 103",   5),
        ClassSchedule("FRI-A6", "Physics",             "10-A", DayOfWeek.FRIDAY,"14:05","14:50","10001","Lab 1",      6),

        ClassSchedule("FRI-B1", "Geography",           "10-B", DayOfWeek.FRIDAY,"09:00","09:45","10003","Room 104",   1),
        ClassSchedule("FRI-B2", "Computer Science",    "10-B", DayOfWeek.FRIDAY,"09:55","10:40","10004","Lab 2",      2),
        ClassSchedule("FRI-B3", "Physics",             "10-B", DayOfWeek.FRIDAY,"10:50","11:35","10001","Lab 1",      3),
        ClassSchedule("FRI-B4", "English Literature",  "10-B", DayOfWeek.FRIDAY,"12:15","13:00","10002","Room 103",   4),
        ClassSchedule("FRI-B5", "Mathematics",         "10-B", DayOfWeek.FRIDAY,"13:10","13:55","10000","Room 101",   5),
        ClassSchedule("FRI-B6", "Biology",             "10-B", DayOfWeek.FRIDAY,"14:05","14:50","10001","Lab 3",      6)
    )

    // ── Free periods (lunch break only — 11:35 to 12:15 every weekday) ────────
    val weekdayFreePeriods: List<FreePeriod> = DayOfWeek.entries
        .filter { it.value <= 5 }
        .map { FreePeriod("11:35", "12:15", it) }

    // ── Helpers ───────────────────────────────────────────────────────────────
    fun teacherName(id: String): String = teachers.firstOrNull { it.id == id }?.name ?: "Faculty"
    fun teacherSubject(id: String): String = teachers.firstOrNull { it.id == id }?.subject ?: ""
    fun studentName(id: String): String = students.firstOrNull { it.id == id }?.name ?: "Student"
    fun studentSection(id: String): String = students.firstOrNull { it.id == id }?.classSection ?: ""

    fun studentsInSection(section: String): List<Student> =
        students.filter { it.classSection == section }

    fun freePeriodsByDay(dayOfWeek: DayOfWeek): List<FreePeriod> =
        weekdayFreePeriods.filter { it.dayOfWeek == dayOfWeek }

    // ── Activity suggestions ──────────────────────────────────────────────────
    val activitySuggestions = listOf(
        ActivitySuggestion("Solve Practice Problems",    "Work through 5 Maths or Physics problems from your textbook.", 20, ActivityCategory.Practice, listOf(StudentGoal.Engineering)),
        ActivitySuggestion("Code a Mini Project",        "Spend time on a small coding challenge or algorithm problem.",  40, ActivityCategory.Study,    listOf(StudentGoal.Engineering)),
        ActivitySuggestion("Revise Physics Formulas",    "Go through your formula sheet and test yourself.",              20, ActivityCategory.Revision,  listOf(StudentGoal.Engineering)),
        ActivitySuggestion("Revise Biology Diagrams",    "Redraw and label key diagrams from your Biology chapter.",      20, ActivityCategory.Revision,  listOf(StudentGoal.Medicine)),
        ActivitySuggestion("Chemistry Reaction Practice","Write out and balance 10 chemical equations from memory.",      20, ActivityCategory.Practice,  listOf(StudentGoal.Medicine)),
        ActivitySuggestion("Read a Medical Article",     "Read a short article on a current health topic.",               40, ActivityCategory.Reading,   listOf(StudentGoal.Medicine)),
        ActivitySuggestion("Free Writing",               "Write a short essay, poem, or journal entry on any topic.",     20, ActivityCategory.Creative,  listOf(StudentGoal.Arts)),
        ActivitySuggestion("Read a Chapter",             "Continue reading your current book or a recommended classic.",  40, ActivityCategory.Reading,   listOf(StudentGoal.Arts)),
        ActivitySuggestion("Vocabulary Builder",         "Learn 10 new words with meanings and use them in sentences.",   20, ActivityCategory.Study,     listOf(StudentGoal.Arts)),
        ActivitySuggestion("Practice Accounting",        "Solve journal entries or balance sheet problems.",              20, ActivityCategory.Practice,  listOf(StudentGoal.Commerce)),
        ActivitySuggestion("Read Business News",         "Skim today's top business headlines and note key takeaways.",   20, ActivityCategory.Reading,   listOf(StudentGoal.Commerce)),
        ActivitySuggestion("Mental Math Drill",          "Practice percentage, ratio, and profit/loss calculations.",     20, ActivityCategory.Practice,  listOf(StudentGoal.Commerce)),
        ActivitySuggestion("Read a Science Abstract",    "Find and read the abstract of a recent research paper.",        20, ActivityCategory.Reading,   listOf(StudentGoal.Research)),
        ActivitySuggestion("Data Analysis Exercise",     "Analyse a small dataset or graph from your textbook.",          20, ActivityCategory.Study,     listOf(StudentGoal.Research)),
        ActivitySuggestion("Experiment Design",          "Write a hypothesis and method for a simple experiment idea.",   40, ActivityCategory.Creative,  listOf(StudentGoal.Research)),
        ActivitySuggestion("Quick Stretch Break",        "Step away, stretch, and reset your focus.",                     20, ActivityCategory.Wellness,  emptyList()),
        ActivitySuggestion("Review Today's Notes",       "Go through notes from your last class and highlight key points.",20,ActivityCategory.Revision,  emptyList()),
        ActivitySuggestion("Plan Your Evening Study",    "Write down what you want to cover in tonight's study session.", 20, ActivityCategory.Study,     emptyList()),
        ActivitySuggestion("Mindful Breathing",          "5 minutes of deep breathing to reduce stress and refocus.",     20, ActivityCategory.Wellness,  emptyList()),
        ActivitySuggestion("Lunch Break Reading",        "Use your lunch break to read something you enjoy.",             40, ActivityCategory.Reading,   emptyList()),
        ActivitySuggestion("Peer Discussion",            "Discuss a topic from today's class with a classmate.",          40, ActivityCategory.Study,     emptyList())
    )

    fun suggestionsFor(goal: StudentGoal?, durationMinutes: Int): List<ActivitySuggestion> =
        activitySuggestions.filter {
            it.durationMinutes <= durationMinutes &&
                (it.goals.isEmpty() || (goal != null && it.goals.contains(goal)))
        }.shuffled().take(3)

    // ── Simulated attendance history for student view ─────────────────────────
    val subjects = listOf(
        "Mathematics", "Physics", "Chemistry", "Biology",
        "English Language", "English Literature",
        "History & Civics", "Geography", "Computer Science", "Physical Education"
    )

    val simulatedStudentAttendance: Map<String, Map<String, Int>> =
        students.associate { student ->
            student.id to subjects.associate { subject -> subject to (68..100).random() }
        }

    val totalClassesPerSubject: Map<String, Int> = mapOf(
        "Mathematics"       to 24,
        "Physics"           to 18,
        "Chemistry"         to 18,
        "Biology"           to 16,
        "English Language"  to 20,
        "English Literature" to 16,
        "History & Civics"   to 14,
        "Geography"         to 14,
        "Computer Science"  to 16,
        "Physical Education" to 12
    )
}
