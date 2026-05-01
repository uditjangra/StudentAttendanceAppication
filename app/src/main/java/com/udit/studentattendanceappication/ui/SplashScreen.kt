package com.udit.studentattendanceappication.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udit.studentattendanceappication.ui.theme.SchoolGreen
import kotlinx.coroutines.delay

/**
 * Splash Screen — shown for 2.5 seconds when the app first opens.
 *
 * Demonstrates LaunchedEffect: a side-effect that runs once when this
 * composable enters the screen. It waits 2.5 seconds then calls onFinished()
 * to navigate to the login screen.
 *
 * Also demonstrates:
 * - animateFloatAsState: smoothly animates the alpha (opacity) from 0 to 1
 * - CircularProgressIndicator: the spinning loading indicator at the bottom
 */
@Composable
fun SplashScreen(onFinished: () -> Unit) {

    // Controls whether the fade-in animation has started
    var startAnimation by remember { mutableStateOf(false) }

    // Animates alpha from 0f → 1f over 1 second when startAnimation becomes true
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "splash_fade"
    )

    // LaunchedEffect runs this block once when the composable first appears.
    // key1 = true means it never re-runs (only runs on first composition).
    LaunchedEffect(key1 = true) {
        startAnimation = true   // trigger the fade-in animation
        delay(2500)             // wait 2.5 seconds
        onFinished()            // navigate to login
    }

    // Full-screen green background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SchoolGreen),
        contentAlignment = Alignment.Center
    ) {
        // Apply the animated alpha to the entire content
        Column(
            modifier = Modifier.alpha(alpha),
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
