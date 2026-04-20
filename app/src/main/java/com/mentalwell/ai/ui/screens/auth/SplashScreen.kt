package com.mentalwell.ai.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.mentalwell.ai.domain.repository_interface.AuthRepository
import com.mentalwell.ai.ui.navigation.Screen
import kotlinx.coroutines.delay

/**
 * Splash Screen composable displaying the logo and navigating based on authentication state.
 */
@Composable
fun SplashScreen(
    navController: NavController,
    authRepository: AuthRepository
) {
    LaunchedEffect(Unit) {
        delay(2000)
        if (authRepository.isLoggedIn()) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "MentalWell AI",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}
