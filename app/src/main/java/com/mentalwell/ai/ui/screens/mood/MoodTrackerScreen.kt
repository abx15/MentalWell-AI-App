package com.mentalwell.ai.ui.screens.mood

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mentalwell.ai.data.model.MoodType

@Composable
fun MoodTrackerScreen(
    viewModel: MoodViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val todayMood by viewModel.todayMood.collectAsState()
    val selectedMood by viewModel.selectedMood.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var noteText by remember(todayMood) { mutableStateOf(todayMood?.note ?: "") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Mood Row utilizing entries
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MoodType.entries.forEach { mood ->
                    val isSelected = selectedMood == mood
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.25f else 1.0f,
                        animationSpec = tween(durationMillis = 300), 
                        label = "scaleAnim"
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .scale(scale)
                            .background(
                                color = if (isSelected) Color(mood.color).copy(alpha = 0.2f) else Color.Transparent,
                                shape = CircleShape
                            )
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) Color(mood.color) else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { viewModel.selectMood(mood) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = mood.emoji, fontSize = 28.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Selected Mood Label
            if (selectedMood != null) {
                Text(
                    text = selectedMood!!.label,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(selectedMood!!.color)
                )
            } else {
                Text("Select a mood", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Note Field
            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Add a note (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )

            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.saveMood(noteText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = selectedMood != null && !isLoading,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), 
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    val btnText = if (todayMood != null) "Update Mood" else "Save Mood"
                    Text(btnText)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(onClick = { onNavigateBack() }) {
                Text("Back to Dashboard")
            }
        }
    }
}
