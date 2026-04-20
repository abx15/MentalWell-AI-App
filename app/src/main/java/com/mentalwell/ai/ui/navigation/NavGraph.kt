package com.mentalwell.ai.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mentalwell.ai.domain.repository_interface.AuthRepository
import com.mentalwell.ai.ui.screens.auth.LoginScreen
import com.mentalwell.ai.ui.screens.auth.SignUpScreen
import com.mentalwell.ai.ui.screens.auth.SplashScreen
import com.mentalwell.ai.ui.screens.mood.MoodTrackerScreen

/**
 * Sealed class defining all the routes in the application.
 */
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Home : Screen("home")
    data object MoodTracker : Screen("mood_tracker")
}

@Composable
fun HomeScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Home Screen (Dashboard)")
            Button(onClick = { navController.navigate(Screen.MoodTracker.route) }) {
                Text("Track Mood")
            }
        }
    }
}

@Composable
fun MentalWellNavGraph(
    navController: NavHostController = rememberNavController(),
    authRepository: AuthRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController, authRepository = authRepository)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.MoodTracker.route) {
            MoodTrackerScreen(onNavigateBack = { navController.navigateUp() })
        }
    }
}
