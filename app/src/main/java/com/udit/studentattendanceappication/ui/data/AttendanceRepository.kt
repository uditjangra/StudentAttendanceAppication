package com.udit.studentattendanceappication.ui.data

import com.udit.studentattendanceappication.ui.model.ActivitySuggestion
import com.udit.studentattendanceappication.ui.model.ClassSchedule
import com.udit.studentattendanceappication.ui.model.FreePeriod
import com.udit.studentattendanceappication.ui.model.Student
import com.udit.studentattendanceappication.ui.model.Teacher
import java.time.DayOfWeek

object AttendanceRepository {

    // ── Teachers (only 10000 is the demo login) ───────────────────────────────
    val teachers = listOf(
        Teacher(
            id = "10000", name = "Aarav Mehta", subject = "Mathematics",
            age = 38, qualification = "M.Sc. Mathematics, B.Ed",
            experience = "14 years", phone = "+91 98765 43210",
            email = "aarav.mehta@school.edu", classTeacherOf = "10-A",
            subjectsTaught = listOf("Mathematics", "Applied Mathematics"),
            joinYear = "2010"
        ),
        Teacher(
            id = "10001", name = "Neha Sharma", subject = "Science",
            age = 34, qualification = "M.Sc. Physics, B.Ed",
            experience = "10 years", phone = "+91 98765 43211",
            email = "neha.sharma@school.edu", classTeacherOf = "",
            subjectsTaught = listOf("Physics", "Chemistry", "Biology"),
            joinYear = "2014"
        ),
        Teacher(
            id = "10002", name = "Rohan Iyer", subject = "English",
            age = 41, qualification = "M.A. English Literature, B.Ed",
            experience = "17 years", phone = "+91 98765 43212",
            email = "rohan.iyer@school.edu", classTeacherOf = "10-B",
            subjectsTaught = listOf("English Language", "English Literature"),
            joinYear = "2007"
        ),
        Teacher(
            id = "10003", name = "Priya Nair", subject = "Social Studies",
            age = 36, qualification = "M.A. History, B.Ed",
            experience = "12 years", phone = "+91 98765 43213",
            email = "priya.nair@school.edu", classTeacherOf = "",
            subjectsTaught = listOf("History & Civics", "Geography"),
            joinYear = "2012"
        ),
        Teacher(
            id = "10004", name = "Suresh Pillai", subject = "Computer Science",
            age = 32, qualification = "M.Tech Computer Science, B.Ed",
            experience = "8 years", phone = "+91 98765 43214",
            email = "suresh.pillai@school.edu", classTeacherOf = "",
            subjectsTaught = listOf("Computer Science", "Physical Education"),
            joinYear = "2016"
        )
    )

    // ── Students (only 12301 is the demo login) ───────────────────────────────
    val students: List<Student> = listOf(
        // Class 10-A
        Student(1,  "12301", "Aisha Kapoor",    "10-A",
            rollNumber = "10A01", dateOfBirth = "12 Mar 2009", gender = "Female",
            bloodGroup = "B+", address = "42, Green Park, New Delhi - 110016",
            parentName = "Rajesh Kapoor", parentPhone = "+91 99887 76655",
            parentOccupation = "Business", sportsHouse = "Blue House",
            sports = "Badminton, Athletics", busRoute = "Route 3", admissionYear = "2022"),
        Student(2,  "12302", "Vivaan Singh",    "10-A",
            rollNumber = "10A02", dateOfBirth = "5 Jul 2009", gender = "Male",
            bloodGroup = "O+", address = "15, Sector 12, Noida - 201301",
            parentName = "Harpreet Singh", parentPhone = "+91 99887 76656",
            parentOccupation = "Engineer", sportsHouse = "Red House",
            sports = "Cricket, Football", busRoute = "Route 1", admissionYear = "2022"),
        Student(3,  "12303", "Anika Rao",       "10-A",
            rollNumber = "10A03", dateOfBirth = "22 Nov 2008", gender = "Female",
            bloodGroup = "A+", address = "8, MG Road, Bengaluru - 560001",
            parentName = "Suresh Rao", parentPhone = "+91 99887 76657",
            parentOccupation = "Doctor", sportsHouse = "Green House",
            sports = "Swimming, Tennis", busRoute = "Route 2", admissionYear = "2022"),
        Student(4,  "12304", "Kabir Nair",      "10-A",
            rollNumber = "10A04", dateOfBirth = "18 Feb 2009", gender = "Male",
            bloodGroup = "AB+", address = "33, Pali Hill, Mumbai - 400050",
            parentName = "Mohan Nair", parentPhone = "+91 99887 76658",
            parentOccupation = "Lawyer", sportsHouse = "Yellow House",
            sports = "Basketball, Chess", busRoute = "Route 4", admissionYear = "2022"),
        Student(5,  "12305", "Meera Patel",     "10-A",
            rollNumber = "10A05", dateOfBirth = "9 Sep 2009", gender = "Female",
            bloodGroup = "O-", address = "21, CG Road, Ahmedabad - 380009",
            parentName = "Dinesh Patel", parentPhone = "+91 99887 76659",
            parentOccupation = "Accountant", sportsHouse = "Blue House",
            sports = "Volleyball, Yoga", busRoute = "Route 3", admissionYear = "2022"),
        Student(6,  "12306", "Arjun Verma",     "10-A",
            rollNumber = "10A06", dateOfBirth = "14 Jan 2009", gender = "Male",
            bloodGroup = "B-", address = "7, Civil Lines, Jaipur - 302006",
            parentName = "Vikram Verma", parentPhone = "+91 99887 76660",
            parentOccupation = "Teacher", sportsHouse = "Red House",
            sports = "Football, Kabaddi", busRoute = "Route 1", admissionYear = "2022"),
        Student(7,  "12307", "Ishita Sen",      "10-A",
            rollNumber = "10A07", dateOfBirth = "30 Apr 2009", gender = "Female",
            bloodGroup = "A-", address = "56, Park Street, Kolkata - 700016",
            parentName = "Arnab Sen", parentPhone = "+91 99887 76661",
            parentOccupation = "Professor", sportsHouse = "Green House",
            sports = "Table Tennis, Dance", busRoute = "Route 2", admissionYear = "2022"),
        Student(8,  "12308", "Reyansh Khanna",  "10-A",
            rollNumber = "10A08", dateOfBirth = "3 Aug 2009", gender = "Male",
            bloodGroup = "O+", address = "12, Model Town, Ludhiana - 141002",
            parentName = "Sanjeev Khanna", parentPhone = "+91 99887 76662",
            parentOccupation = "Businessman", sportsHouse = "Yellow House",
            sports = "Hockey, Athletics", busRoute = "Route 5", admissionYear = "2022"),
        Student(9,  "12309", "Diya Menon",      "10-A",
            rollNumber = "10A09", dateOfBirth = "25 Dec 2008", gender = "Female",
            bloodGroup = "B+", address = "4, Vyttila, Kochi - 682019",
            parentName = "Rajan Menon", parentPhone = "+91 99887 76663",
            parentOccupation = "Pilot", sportsHouse = "Blue House",
            sports = "Swimming, Gymnastics", busRoute = "Route 3", admissionYear = "2022"),
        Student(10, "12310", "Advait Joshi",    "10-A",
            rollNumber = "10A10", dateOfBirth = "17 Jun 2009", gender = "Male",
            bloodGroup = "AB-", address = "9, Shivaji Nagar, Pune - 411005",
            parentName = "Prakash Joshi", parentPhone = "+91 99887 76664",
            parentOccupation = "Architect", sportsHouse = "Red House",
            sports = "Cricket, Carrom", busRoute = "Route 4", admissionYear = "2022"),
        // Class 10-B
        Student(1,  "12311", "Sara Bansal",     "10-B",
            rollNumber = "10B01", dateOfBirth = "8 Oct 2009", gender = "Female",
            bloodGroup = "O+", address = "18, Rajouri Garden, New Delhi - 110027",
            parentName = "Amit Bansal", parentPhone = "+91 99887 76665",
            parentOccupation = "CA", sportsHouse = "Green House",
            sports = "Badminton, Skating", busRoute = "Route 2", admissionYear = "2022"),
        Student(2,  "12312", "Krish Malhotra",  "10-B",
            rollNumber = "10B02", dateOfBirth = "20 May 2009", gender = "Male",
            bloodGroup = "A+", address = "27, Sector 22, Chandigarh - 160022",
            parentName = "Deepak Malhotra", parentPhone = "+91 99887 76666",
            parentOccupation = "IAS Officer", sportsHouse = "Yellow House",
            sports = "Football, Swimming", busRoute = "Route 1", admissionYear = "2022"),
        Student(3,  "12313", "Myra Desai",      "10-B",
            rollNumber = "10B03", dateOfBirth = "11 Mar 2009", gender = "Female",
            bloodGroup = "B+", address = "3, Navrangpura, Ahmedabad - 380009",
            parentName = "Nitin Desai", parentPhone = "+91 99887 76667",
            parentOccupation = "Pharmacist", sportsHouse = "Blue House",
            sports = "Yoga, Dance", busRoute = "Route 3", admissionYear = "2022"),
        Student(4,  "12314", "Yash Tiwari",     "10-B",
            rollNumber = "10B04", dateOfBirth = "29 Jul 2009", gender = "Male",
            bloodGroup = "O-", address = "14, Hazratganj, Lucknow - 226001",
            parentName = "Ramesh Tiwari", parentPhone = "+91 99887 76668",
            parentOccupation = "Police Officer", sportsHouse = "Red House",
            sports = "Kabaddi, Athletics", busRoute = "Route 5", admissionYear = "2022"),
        Student(5,  "12315", "Kiara Sethi",     "10-B",
            rollNumber = "10B05", dateOfBirth = "2 Feb 2009", gender = "Female",
            bloodGroup = "AB+", address = "6, Defence Colony, New Delhi - 110024",
            parentName = "Rohit Sethi", parentPhone = "+91 99887 76669",
            parentOccupation = "Army Officer", sportsHouse = "Green House",
            sports = "Tennis, Horse Riding", busRoute = "Route 2", admissionYear = "2022"),
        Student(6,  "12316", "Atharv Kulkarni", "10-B",
            rollNumber = "10B06", dateOfBirth = "16 Nov 2008", gender = "Male",
            bloodGroup = "B-", address = "22, Deccan Gymkhana, Pune - 411004",
            parentName = "Sunil Kulkarni", parentPhone = "+91 99887 76670",
            parentOccupation = "Engineer", sportsHouse = "Yellow House",
            sports = "Cricket, Chess", busRoute = "Route 4", admissionYear = "2022"),
        Student(7,  "12317", "Riya Chawla",     "10-B",
            rollNumber = "10B07", dateOfBirth = "7 Sep 2009", gender = "Female",
            bloodGroup = "A-", address = "31, Lajpat Nagar, New Delhi - 110024",
            parentName = "Manoj Chawla", parentPhone = "+91 99887 76671",
            parentOccupation = "Journalist", sportsHouse = "Blue House",
            sports = "Basketball, Painting", busRoute = "Route 1", admissionYear = "2022"),
        Student(8,  "12318", "Vihaan Arora",    "10-B",
            rollNumber = "10B08", dateOfBirth = "23 Apr 2009", gender = "Male",
            bloodGroup = "O+", address = "5, Vasant Vihar, New Delhi - 110057",
            parentName = "Gaurav Arora", parentPhone = "+91 99887 76672",
            parentOccupation = "Banker", sportsHouse = "Red House",
            sports = "Football, Table Tennis", busRoute = "Route 3", admissionYear = "2022"),
        Student(9,  "12319", "Naina Dutta",     "10-B",
            rollNumber = "10B09", dateOfBirth = "19 Jan 2009", gender = "Female",
            bloodGroup = "B+", address = "10, Salt Lake, Kolkata - 700091",
            parentName = "Subhash Dutta", parentPhone = "+91 99887 76673",
            parentOccupation = "Professor", sportsHouse = "Green House",
            sports = "Swimming, Debate", busRoute = "Route 2", admissionYear = "2022"),
        Student(10, "12320", "Parth Gill",      "10-B",
            rollNumber = "10B10", dateOfBirth = "6 Jun 2009", gender = "Male",
            bloodGroup = "AB+", address = "17, Model Town, Amritsar - 143001",
            parentName = "Gurpreet Gill", parentPhone = "+91 99887 76674",
            parentOccupation = "Farmer", sportsHouse = "Yellow House",
            sports = "Hockey, Wrestling", busRoute = "Route 5", admissionYear = "2022")
    )

    // ── School Timetable ──────────────────────────────────────────────────────
    val schedule: List<ClassSchedule> = listOf(
        // MONDAY
        ClassSchedule("MON-A1","Mathematics",       "10-A",DayOfWeek.MONDAY,   "09:00","09:45","10000","Room 101",1),
        ClassSchedule("MON-A2","Physics",            "10-A",DayOfWeek.MONDAY,   "09:55","10:40","10001","Lab 1",   2),
        ClassSchedule("MON-A3","English Language",   "10-A",DayOfWeek.MONDAY,   "10:50","11:35","10002","Room 103",3),
        ClassSchedule("MON-A4","History & Civics",   "10-A",DayOfWeek.MONDAY,   "12:15","13:00","10003","Room 104",4),
        ClassSchedule("MON-A5","Computer Science",   "10-A",DayOfWeek.MONDAY,   "13:10","13:55","10004","Lab 2",   5),
        ClassSchedule("MON-A6","Geography",          "10-A",DayOfWeek.MONDAY,   "14:05","14:50","10003","Room 104",6),
        ClassSchedule("MON-B1","English Language",   "10-B",DayOfWeek.MONDAY,   "09:00","09:45","10002","Room 103",1),
        ClassSchedule("MON-B2","Mathematics",        "10-B",DayOfWeek.MONDAY,   "09:55","10:40","10000","Room 101",2),
        ClassSchedule("MON-B3","Chemistry",          "10-B",DayOfWeek.MONDAY,   "10:50","11:35","10001","Lab 1",   3),
        ClassSchedule("MON-B4","Computer Science",   "10-B",DayOfWeek.MONDAY,   "12:15","13:00","10004","Lab 2",   4),
        ClassSchedule("MON-B5","History & Civics",   "10-B",DayOfWeek.MONDAY,   "13:10","13:55","10003","Room 104",5),
        ClassSchedule("MON-B6","Physical Education", "10-B",DayOfWeek.MONDAY,   "14:05","14:50","10004","Ground",  6),
        // TUESDAY
        ClassSchedule("TUE-A1","Chemistry",          "10-A",DayOfWeek.TUESDAY,  "09:00","09:45","10001","Lab 1",   1),
        ClassSchedule("TUE-A2","Mathematics",        "10-A",DayOfWeek.TUESDAY,  "09:55","10:40","10000","Room 101",2),
        ClassSchedule("TUE-A3","Biology",            "10-A",DayOfWeek.TUESDAY,  "10:50","11:35","10001","Lab 3",   3),
        ClassSchedule("TUE-A4","English Literature", "10-A",DayOfWeek.TUESDAY,  "12:15","13:00","10002","Room 103",4),
        ClassSchedule("TUE-A5","Geography",          "10-A",DayOfWeek.TUESDAY,  "13:10","13:55","10003","Room 104",5),
        ClassSchedule("TUE-A6","Physical Education", "10-A",DayOfWeek.TUESDAY,  "14:05","14:50","10004","Ground",  6),
        ClassSchedule("TUE-B1","Physics",            "10-B",DayOfWeek.TUESDAY,  "09:00","09:45","10001","Lab 1",   1),
        ClassSchedule("TUE-B2","English Literature", "10-B",DayOfWeek.TUESDAY,  "09:55","10:40","10002","Room 103",2),
        ClassSchedule("TUE-B3","Mathematics",        "10-B",DayOfWeek.TUESDAY,  "10:50","11:35","10000","Room 101",3),
        ClassSchedule("TUE-B4","Biology",            "10-B",DayOfWeek.TUESDAY,  "12:15","13:00","10001","Lab 3",   4),
        ClassSchedule("TUE-B5","Computer Science",   "10-B",DayOfWeek.TUESDAY,  "13:10","13:55","10004","Lab 2",   5),
        ClassSchedule("TUE-B6","Geography",          "10-B",DayOfWeek.TUESDAY,  "14:05","14:50","10003","Room 104",6),
        // WEDNESDAY
        ClassSchedule("WED-A1","Physics",            "10-A",DayOfWeek.WEDNESDAY,"09:00","09:45","10001","Lab 1",   1),
        ClassSchedule("WED-A2","English Literature", "10-A",DayOfWeek.WEDNESDAY,"09:55","10:40","10002","Room 103",2),
        ClassSchedule("WED-A3","Mathematics",        "10-A",DayOfWeek.WEDNESDAY,"10:50","11:35","10000","Room 101",3),
        ClassSchedule("WED-A4","Biology",            "10-A",DayOfWeek.WEDNESDAY,"12:15","13:00","10001","Lab 3",   4),
        ClassSchedule("WED-A5","Computer Science",   "10-A",DayOfWeek.WEDNESDAY,"13:10","13:55","10004","Lab 2",   5),
        ClassSchedule("WED-A6","Physical Education", "10-A",DayOfWeek.WEDNESDAY,"14:05","14:50","10004","Ground",  6),
        ClassSchedule("WED-B1","Mathematics",        "10-B",DayOfWeek.WEDNESDAY,"09:00","09:45","10000","Room 101",1),
        ClassSchedule("WED-B2","Chemistry",          "10-B",DayOfWeek.WEDNESDAY,"09:55","10:40","10001","Lab 1",   2),
        ClassSchedule("WED-B3","English Language",   "10-B",DayOfWeek.WEDNESDAY,"10:50","11:35","10002","Room 103",3),
        ClassSchedule("WED-B4","History & Civics",   "10-B",DayOfWeek.WEDNESDAY,"12:15","13:00","10003","Room 104",4),
        ClassSchedule("WED-B5","Biology",            "10-B",DayOfWeek.WEDNESDAY,"13:10","13:55","10001","Lab 3",   5),
        ClassSchedule("WED-B6","Geography",          "10-B",DayOfWeek.WEDNESDAY,"14:05","14:50","10003","Room 104",6),
        // THURSDAY
        ClassSchedule("THU-A1","English Language",   "10-A",DayOfWeek.THURSDAY, "09:00","09:45","10002","Room 103",1),
        ClassSchedule("THU-A2","History & Civics",   "10-A",DayOfWeek.THURSDAY, "09:55","10:40","10003","Room 104",2),
        ClassSchedule("THU-A3","Chemistry",          "10-A",DayOfWeek.THURSDAY, "10:50","11:35","10001","Lab 1",   3),
        ClassSchedule("THU-A4","Mathematics",        "10-A",DayOfWeek.THURSDAY, "12:15","13:00","10000","Room 101",4),
        ClassSchedule("THU-A5","Biology",            "10-A",DayOfWeek.THURSDAY, "13:10","13:55","10001","Lab 3",   5),
        ClassSchedule("THU-A6","Computer Science",   "10-A",DayOfWeek.THURSDAY, "14:05","14:50","10004","Lab 2",   6),
        ClassSchedule("THU-B1","Chemistry",          "10-B",DayOfWeek.THURSDAY, "09:00","09:45","10001","Lab 1",   1),
        ClassSchedule("THU-B2","Mathematics",        "10-B",DayOfWeek.THURSDAY, "09:55","10:40","10000","Room 101",2),
        ClassSchedule("THU-B3","English Language",   "10-B",DayOfWeek.THURSDAY, "10:50","11:35","10002","Room 103",3),
        ClassSchedule("THU-B4","Geography",          "10-B",DayOfWeek.THURSDAY, "12:15","13:00","10003","Room 104",4),
        ClassSchedule("THU-B5","Physical Education", "10-B",DayOfWeek.THURSDAY, "13:10","13:55","10004","Ground",  5),
        ClassSchedule("THU-B6","Computer Science",   "10-B",DayOfWeek.THURSDAY, "14:05","14:50","10004","Lab 2",   6),
        // FRIDAY
        ClassSchedule("FRI-A1","Computer Science",   "10-A",DayOfWeek.FRIDAY,   "09:00","09:45","10004","Lab 2",   1),
        ClassSchedule("FRI-A2","Biology",            "10-A",DayOfWeek.FRIDAY,   "09:55","10:40","10001","Lab 3",   2),
        ClassSchedule("FRI-A3","Geography",          "10-A",DayOfWeek.FRIDAY,   "10:50","11:35","10003","Room 104",3),
        ClassSchedule("FRI-A4","Mathematics",        "10-A",DayOfWeek.FRIDAY,   "12:15","13:00","10000","Room 101",4),
        ClassSchedule("FRI-A5","English Language",   "10-A",DayOfWeek.FRIDAY,   "13:10","13:55","10002","Room 103",5),
        ClassSchedule("FRI-A6","Physics",            "10-A",DayOfWeek.FRIDAY,   "14:05","14:50","10001","Lab 1",   6),
        ClassSchedule("FRI-B1","Geography",          "10-B",DayOfWeek.FRIDAY,   "09:00","09:45","10003","Room 104",1),
        ClassSchedule("FRI-B2","Computer Science",   "10-B",DayOfWeek.FRIDAY,   "09:55","10:40","10004","Lab 2",   2),
        ClassSchedule("FRI-B3","Physics",            "10-B",DayOfWeek.FRIDAY,   "10:50","11:35","10001","Lab 1",   3),
        ClassSchedule("FRI-B4","English Literature", "10-B",DayOfWeek.FRIDAY,   "12:15","13:00","10002","Room 103",4),
        ClassSchedule("FRI-B5","Mathematics",        "10-B",DayOfWeek.FRIDAY,   "13:10","13:55","10000","Room 101",5),
        ClassSchedule("FRI-B6","Biology",            "10-B",DayOfWeek.FRIDAY,   "14:05","14:50","10001","Lab 3",   6)
    )

    val weekdayFreePeriods: List<FreePeriod> = DayOfWeek.entries
        .filter { it.value <= 5 }
        .map { FreePeriod("11:35", "12:15", it) }

    fun teacherName(id: String): String = teachers.firstOrNull { it.id == id }?.name ?: "Faculty"
    fun teacherSubject(id: String): String = teachers.firstOrNull { it.id == id }?.subject ?: ""
    fun studentName(id: String): String = students.firstOrNull { it.id == id }?.name ?: "Student"
    fun studentSection(id: String): String = students.firstOrNull { it.id == id }?.classSection ?: ""
    fun studentsInSection(section: String): List<Student> = students.filter { it.classSection == section }
    fun freePeriodsByDay(dayOfWeek: DayOfWeek): List<FreePeriod> = weekdayFreePeriods.filter { it.dayOfWeek == dayOfWeek }

    val activitySuggestions = listOf(
        // Each suggestion has: title, description, duration in minutes, category name, category color
        ActivitySuggestion("Solve Practice Problems",    "Work through 5 Maths or Physics problems from your textbook.", 20, "Practice", 0xFF4CAF7A),
        ActivitySuggestion("Review Today's Notes",       "Go through notes from your last class and highlight key points.", 20, "Revision", 0xFFF2A65A),
        ActivitySuggestion("Quick Stretch Break",        "Step away, stretch, and reset your focus.", 20, "Wellness", 0xFFE66B6B),
        ActivitySuggestion("Plan Your Evening Study",    "Write down what you want to cover in tonight's study session.", 20, "Study", 0xFF3C8D84),
        ActivitySuggestion("Mindful Breathing",          "5 minutes of deep breathing to reduce stress and refocus.", 20, "Wellness", 0xFFE66B6B),
        ActivitySuggestion("Lunch Break Reading",        "Use your lunch break to read something you enjoy.", 40, "Reading", 0xFF7B68EE),
        ActivitySuggestion("Peer Discussion",            "Discuss a topic from today's class with a classmate.", 40, "Study", 0xFF3C8D84),
        ActivitySuggestion("Vocabulary Builder",         "Learn 10 new words with meanings and use them in sentences.", 20, "Study", 0xFF3C8D84),
        ActivitySuggestion("Revise Formulas",            "Go through your formula sheet for any subject and test yourself.", 20, "Revision", 0xFFF2A65A),
        ActivitySuggestion("Read a Chapter",             "Continue reading your current book or a recommended text.", 40, "Reading", 0xFF7B68EE)
    )

    fun suggestionsFor(durationMinutes: Int): List<ActivitySuggestion> =
        activitySuggestions.filter { it.durationMinutes <= durationMinutes }.shuffled().take(3)

    val subjects = listOf(
        "Mathematics","Physics","Chemistry","Biology",
        "English Language","English Literature",
        "History & Civics","Geography","Computer Science","Physical Education"
    )

    val simulatedStudentAttendance: Map<String, Map<String, Int>> =
        students.associate { s -> s.id to subjects.associate { sub -> sub to (68..100).random() } }

    val totalClassesPerSubject: Map<String, Int> = mapOf(
        "Mathematics" to 24, "Physics" to 18, "Chemistry" to 18, "Biology" to 16,
        "English Language" to 20, "English Literature" to 16,
        "History & Civics" to 14, "Geography" to 14,
        "Computer Science" to 16, "Physical Education" to 12
    )
}
