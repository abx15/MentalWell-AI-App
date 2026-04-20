package com.mentalwell.ai.ui.screens.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalWriteScreen(
    journalId: String?,
    onNavigateBack: () -> Unit,
    viewModel: JournalViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    val selectedJournal by viewModel.selectedJournal.collectAsState()

    LaunchedEffect(journalId) {
        if (journalId != null) viewModel.loadJournalDetails(journalId)
    }

    LaunchedEffect(selectedJournal) {
        if (journalId != null && selectedJournal != null && selectedJournal!!.id == journalId) {
            title = selectedJournal!!.title
            text = selectedJournal!!.text
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is JournalUiState.Success) {
            delay(2000)
            viewModel.clearState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Write Entry") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.clearState(); onNavigateBack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.saveJournal(journalId, title, text) }) {
                        Text("Save", fontWeight = FontWeight.Bold, color = Color(0xFF008080))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(), placeholder = { Text("Give it a title...") },
                    textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                    )
                )
                HorizontalDivider()
                TextField(
                    value = text, onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth().weight(1f), placeholder = { Text("How are you feeling?") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Text(
                    text = "${text.trim().split("\\s+".toRegex()).count { it.isNotEmpty() }} words",
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                    color = Color.Gray, style = MaterialTheme.typography.labelSmall
                )
            }

            if (uiState is JournalUiState.Analyzing || uiState is JournalUiState.Saving) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Analyzing your mood...", color = Color.White)
                    }
                }
            } else if (uiState is JournalUiState.Success) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large).padding(24.dp)) {
                        Icon(Icons.Default.Check, "Success", tint = Color(0xFF008080), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Successfully saved & analyzed!", fontWeight = FontWeight.Bold)
                    }
                }
            } else if (uiState is JournalUiState.Error) {
                // simple fallback presentation.
                Text("Error: ${(uiState as JournalUiState.Error).message}", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
