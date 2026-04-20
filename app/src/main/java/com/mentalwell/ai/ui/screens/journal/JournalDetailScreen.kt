package com.mentalwell.ai.ui.screens.journal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mentalwell.ai.ui.components.EmotionChip
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalDetailScreen(
    journalId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    viewModel: JournalViewModel = hiltViewModel()
) {
    val journal by viewModel.selectedJournal.collectAsState()

    LaunchedEffect(journalId) { viewModel.loadJournalDetails(journalId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { /* Implement native Share intent */ }) { Icon(Icons.Default.Share, "Share") }
                    IconButton(onClick = { onNavigateToEdit(journalId) }) { Icon(Icons.Default.Edit, "Edit") }
                }
            )
        }
    ) { paddingValues ->
        if (journal != null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                Text(journal!!.title, fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 34.sp)
                Spacer(modifier = Modifier.height(8.dp))
                
                val sdf = SimpleDateFormat("EEEE, MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
                Text(sdf.format(Date(journal!!.date)), color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(journal!!.text, fontSize = 16.sp, lineHeight = 24.sp)
                Spacer(modifier = Modifier.height(32.dp))

                if (!journal!!.aiEmotion.isNullOrEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("AI Analysis", fontWeight = FontWeight.Bold, color = Color(0xFF008080))
                            Spacer(modifier = Modifier.height(8.dp))
                            Row { Text("Detected Emotion: "); Spacer(Modifier.width(8.dp)); EmotionChip(journal!!.aiEmotion) }
                            if (journal!!.moodScore != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Mood Score: ${journal!!.moodScore}/10")
                            }
                            if (journal!!.suggestions.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Suggestions:", fontWeight = FontWeight.SemiBold)
                                journal!!.suggestions.forEach {
                                    Text("• $it", modifier = Modifier.padding(start = 8.dp, top = 4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
