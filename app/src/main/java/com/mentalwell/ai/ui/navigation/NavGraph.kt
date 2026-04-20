package com.mentalwell.ai.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mentalwell.ai.domain.repository_interface.AuthRepository
import com.mentalwell.ai.ui.screens.auth.LoginScreen
import com.mentalwell.ai.ui.screens.auth.SignUpScreen
import com.mentalwell.ai.ui.screens.auth.SplashScreen

/**
 * Sealed class defining all the routes in the application.
 */
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Home : Screen("home")
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home Screen (Dashboard)")
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
            HomeScreen()
        }
    }
}
