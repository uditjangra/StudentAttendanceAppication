package com.udit.studentattendanceappication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udit.studentattendanceappication.ui.theme.SchoolGreen
import kotlinx.coroutines.delay

/**
 * SplashScreen — the first screen shown when the app opens.
 *
 * It shows the school logo and app name for 2.5 seconds,
 * then automatically navigates to the login screen.
 *
 * LaunchedEffect is used here to run a timed action (delay + navigate).
 * LaunchedEffect(key1 = true) means: run this block once when the screen appears.
 * Inside the block, delay(2500) waits 2.5 seconds, then onFinished() is called.
 *
 * CircularProgressIndicator shows a spinning loading ring at the bottom.
 */
@Composable
fun SplashScreen(onFinished: () -> Unit) {

    // LaunchedEffect runs this coroutine block once when the composable first appears.
    // After 2.5 seconds it calls onFinished() to go to the login screen.
    LaunchedEffect(key1 = true) {
        delay(2500)     // wait 2.5 seconds
        onFinished()    // navigate to login
    }

    // Full-screen green background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SchoolGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // School icon inside a white circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.School,
                    contentDescription = "School Logo",
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Smart Attendance",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 28.sp
            )

            Text(
                text = "School Management System",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(24.dp))

            // CircularProgressIndicator — the spinning loading ring
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
