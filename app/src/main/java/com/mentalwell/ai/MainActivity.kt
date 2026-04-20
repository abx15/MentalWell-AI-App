package com.mentalwell.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mentalwell.ai.domain.repository_interface.AuthRepository
import com.mentalwell.ai.ui.navigation.MentalWellNavGraph
import com.mentalwell.ai.ui.theme.MentalWellTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * MainActivity serves as the entry point for the MentalWell AI application,
 * setting up the primary Jetpack Compose surface and navigation graph.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentalWellTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MentalWellNavGraph(authRepository = authRepository)
                }
            }
        }
    }
}
