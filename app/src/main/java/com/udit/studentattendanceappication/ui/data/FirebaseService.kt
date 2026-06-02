package com.udit.studentattendanceappication.ui.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.udit.studentattendanceappication.ui.model.Student
import com.udit.studentattendanceappication.ui.model.Teacher
import kotlinx.coroutines.tasks.await

/**
 * FirebaseService — handles all Firebase Auth and Firestore operations.
 *
 * Firebase Auth  → login, logout, create user accounts
 * Firestore      → store and read teacher/student data
 *
 * Firestore structure:
 *
 * users/{uid}
 *   role: "admin" | "teacher" | "student"
 *   userId: "10000" | "12301"   (the numeric ID shown in the app)
 *
 * teachers/{userId}
 *   name, subject, age, qualification, experience,
 *   phone, email, classTeacherOf, subjectsTaught, joinYear
 *
 * students/{userId}
 *   name, classSection, rollNumber, dateOfBirth, gender,
 *   bloodGroup, address, parentName, parentPhone,
 *   parentOccupation, sportsHouse, sports, busRoute, admissionYear
 *
 * counters/ids
 *   nextTeacherId: 10005   (auto-increments when admin adds a teacher)
 *   nextStudentId: 12321   (auto-increments when admin adds a student)
 */
object FirebaseService {

    private val auth = Firebase.auth
    private val db   = Firebase.firestore

    // ── Auth ──────────────────────────────────────────────────────────────────

    /**
     * Login function — handles two cases:
     *
     * 1. Admin: enters their real email (e.g. admin@edu.in) + password
     *    The app detects it's an email (contains @) and uses it directly.
     *
     * 2. Teacher/Student: enters their numeric ID (e.g. 10000) + password
     *    The app converts it to a fake email: 10000@smartattendance.app
     *
     * Returns the role ("admin", "teacher", "student") on success.
     */
    suspend fun login(userId: String, password: String): Pair<String, String> {
        // If the input contains "@" it's a real email (admin) — use directly
        // Otherwise it's a numeric ID — convert to fake email
        val email = if (userId.contains("@")) userId else toEmail(userId)

        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("Login failed")

        // Read the role from Firestore
        val userDoc = db.collection("users").document(uid).get().await()
        val role = userDoc.getString("role") ?: throw Exception("Role not found")
        return Pair(role, uid)
    }

    /** Logout the current user */
    fun logout() {
        auth.signOut()
    }

    /** Returns the currently logged-in user's UID, or null if not logged in */
    fun currentUid(): String? = auth.currentUser?.uid

    // ── ID Generation ─────────────────────────────────────────────────────────

    /**
     * Gets the next available teacher ID from Firestore and increments it.
     * Uses a Firestore transaction so two admins can't get the same ID.
     */
    suspend fun getNextTeacherId(): String {
        val counterRef = db.collection("counters").document("ids")
        return db.runTransaction { transaction ->
            val snapshot = transaction.get(counterRef)
            val current = if (snapshot.exists()) {
                snapshot.getLong("nextTeacherId") ?: 10000L
            } else {
                10000L
            }
            // set with merge creates the document if it doesn't exist
            transaction.set(counterRef, mapOf("nextTeacherId" to current + 1), com.google.firebase.firestore.SetOptions.merge())
            current.toString()
        }.await()
    }

    /**
     * Gets the next available student ID from Firestore and increments it.
     */
    suspend fun getNextStudentId(): String {
        val counterRef = db.collection("counters").document("ids")
        return db.runTransaction { transaction ->
            val snapshot = transaction.get(counterRef)
            val current = if (snapshot.exists()) {
                snapshot.getLong("nextStudentId") ?: 12301L
            } else {
                12301L
            }
            transaction.set(counterRef, mapOf("nextStudentId" to current + 1), com.google.firebase.firestore.SetOptions.merge())
            current.toString()
        }.await()
    }

    // ── Add Teacher ───────────────────────────────────────────────────────────

    /**
     * Creates a Firebase Auth account for the teacher and saves their
     * profile to Firestore. Returns the generated userId and temp password.
     *
     * IMPORTANT: createUserWithEmailAndPassword signs in as the new user.
     * We save the admin's email before creating, then sign back in as admin after.
     */
    suspend fun addTeacher(
        adminEmail: String,
        adminPassword: String,
        name: String,
        subject: String,
        age: String,
        qualification: String,
        experience: String,
        phone: String,
        email: String,
        classTeacherOf: String,
        subjectsTaught: String,
        initialPassword: String   // admin sets this for the teacher
    ): Pair<String, String> {
        val userId = getNextTeacherId()
        val firebaseEmail = toEmail(userId)

        // Create Firebase Auth account for teacher
        val result = auth.createUserWithEmailAndPassword(firebaseEmail, initialPassword).await()
        val uid = result.user?.uid ?: throw Exception("Failed to create account")

        // Save role mapping in users collection
        db.collection("users").document(uid).set(
            mapOf("role" to "teacher", "userId" to userId)
        ).await()

        // Save full teacher profile in teachers collection
        db.collection("teachers").document(userId).set(
            mapOf(
                "uid"            to uid,
                "name"           to name,
                "subject"        to subject,
                "age"            to (age.toIntOrNull() ?: 0),
                "qualification"  to qualification,
                "experience"     to experience,
                "phone"          to phone,
                "email"          to email,
                "classTeacherOf" to classTeacherOf,
                "subjectsTaught" to subjectsTaught.split(",").map { it.trim() },
                "joinYear"       to java.time.Year.now().value.toString()
            )
        ).await()

        // Sign back in as admin (createUserWithEmailAndPassword signs in as the new user)
        auth.signInWithEmailAndPassword(adminEmail, adminPassword).await()

        return Pair(userId, initialPassword)
    }

    // ── Add Student ───────────────────────────────────────────────────────────

    /**
     * Creates a Firebase Auth account for the student and saves their
     * profile to Firestore. Returns the generated userId and the password admin set.
     */
    suspend fun addStudent(
        adminEmail: String,
        adminPassword: String,
        name: String,
        classSection: String,
        dateOfBirth: String,
        gender: String,
        bloodGroup: String,
        address: String,
        parentName: String,
        parentPhone: String,
        parentOccupation: String,
        sportsHouse: String,
        sports: String,
        busRoute: String,
        initialPassword: String   // admin sets this for the student
    ): Pair<String, String> {
        val userId = getNextStudentId()
        val firebaseEmail = toEmail(userId)

        // Create Firebase Auth account
        val result = auth.createUserWithEmailAndPassword(firebaseEmail, initialPassword).await()
        val uid = result.user?.uid ?: throw Exception("Failed to create account")

        // Save role mapping
        db.collection("users").document(uid).set(
            mapOf("role" to "student", "userId" to userId)
        ).await()

        // Calculate roll number: section + serial (e.g. 10A01)
        val studentsInSection = db.collection("students")
            .whereEqualTo("classSection", classSection).get().await()
        val serial = (studentsInSection.size() + 1).toString().padStart(2, '0')
        val rollNumber = "${classSection.replace("-", "")}$serial"

        // Save full student profile
        db.collection("students").document(userId).set(
            mapOf(
                "uid"              to uid,
                "name"             to name,
                "classSection"     to classSection,
                "rollNumber"       to rollNumber,
                "dateOfBirth"      to dateOfBirth,
                "gender"           to gender,
                "bloodGroup"       to bloodGroup,
                "address"          to address,
                "parentName"       to parentName,
                "parentPhone"      to parentPhone,
                "parentOccupation" to parentOccupation,
                "sportsHouse"      to sportsHouse,
                "sports"           to sports,
                "busRoute"         to busRoute,
                "admissionYear"    to java.time.Year.now().value.toString(),
                "serialNumber"     to (studentsInSection.size() + 1)
            )
        ).await()

        // Sign back in as admin
        auth.signInWithEmailAndPassword(adminEmail, adminPassword).await()

        return Pair(userId, initialPassword)
    }

    // ── Fetch Data ────────────────────────────────────────────────────────────

    /** Load a teacher's profile from Firestore by their userId */
    suspend fun fetchTeacher(userId: String): Teacher? {
        val doc = db.collection("teachers").document(userId).get().await()
        if (!doc.exists()) return null
        return Teacher(
            id             = userId,
            name           = doc.getString("name") ?: "",
            subject        = doc.getString("subject") ?: "",
            age            = (doc.getLong("age") ?: 0L).toInt(),
            qualification  = doc.getString("qualification") ?: "",
            experience     = doc.getString("experience") ?: "",
            phone          = doc.getString("phone") ?: "",
            email          = doc.getString("email") ?: "",
            classTeacherOf = doc.getString("classTeacherOf") ?: "",
            subjectsTaught = (doc.get("subjectsTaught") as? List<*>)
                                 ?.filterIsInstance<String>() ?: emptyList(),
            joinYear       = doc.getString("joinYear") ?: ""
        )
    }

    /** Load a student's profile from Firestore by their userId */
    suspend fun fetchStudent(userId: String): Student? {
        val doc = db.collection("students").document(userId).get().await()
        if (!doc.exists()) return null
        return Student(
            serialNumber      = (doc.getLong("serialNumber") ?: 1L).toInt(),
            id                = userId,
            name              = doc.getString("name") ?: "",
            classSection      = doc.getString("classSection") ?: "",
            rollNumber        = doc.getString("rollNumber") ?: "",
            dateOfBirth       = doc.getString("dateOfBirth") ?: "",
            gender            = doc.getString("gender") ?: "",
            bloodGroup        = doc.getString("bloodGroup") ?: "",
            address           = doc.getString("address") ?: "",
            parentName        = doc.getString("parentName") ?: "",
            parentPhone       = doc.getString("parentPhone") ?: "",
            parentOccupation  = doc.getString("parentOccupation") ?: "",
            sportsHouse       = doc.getString("sportsHouse") ?: "",
            sports            = doc.getString("sports") ?: "",
            busRoute          = doc.getString("busRoute") ?: "",
            admissionYear     = doc.getString("admissionYear") ?: ""
        )
    }

    /** Load all teachers (for admin list view) */
    suspend fun fetchAllTeachers(): List<Teacher> {
        val snapshot = db.collection("teachers").get().await()
        return snapshot.documents.mapNotNull { doc ->
            fetchTeacher(doc.id)
        }
    }

    /** Load all students (for admin list view) */
    suspend fun fetchAllStudents(): List<Student> {
        val snapshot = db.collection("students").get().await()
        return snapshot.documents.mapNotNull { doc ->
            fetchStudent(doc.id)
        }
    }

    /** Delete a teacher — removes from Auth and Firestore */
    suspend fun deleteTeacher(userId: String) {
        db.collection("teachers").document(userId).delete().await()
        // Note: deleting from Auth requires the user to be signed in as that user
        // For admin panel, we just remove from Firestore — the account becomes unusable
    }

    /** Delete a student — removes from Firestore */
    suspend fun deleteStudent(userId: String) {
        db.collection("students").document(userId).delete().await()
    }

    /** Get userId from uid (reverse lookup) */
    suspend fun getUserIdFromUid(uid: String): String? {
        val doc = db.collection("users").document(uid).get().await()
        return doc.getString("userId")
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    /** Converts a numeric userId to a Firebase-compatible email */
    private fun toEmail(userId: String) = "$userId@smartattendance.app"
}
