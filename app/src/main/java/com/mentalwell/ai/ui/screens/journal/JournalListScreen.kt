package com.mentalwell.ai.ui.screens.journal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mentalwell.ai.data.model.Journal
import com.mentalwell.ai.ui.components.EmotionChip
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalListScreen(
    onNavigateToWrite: (String?) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    viewModel: JournalViewModel = hiltViewModel()
) {
    val journals by viewModel.journals.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var deleteId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadJournals()
    }

    val filteredJournals = journals.filter { it.title.contains(searchQuery, true) || it.text.contains(searchQuery, true) }

    if (deleteId != null) {
        AlertDialog(
            onDismissRequest = { deleteId = null },
            title = { Text("Delete Journal") },
            text = { Text("Are you sure you want to delete this specific entry? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { viewModel.deleteJournal(deleteId!!); deleteId = null }) { 
                    Text("Delete", color = MaterialTheme.colorScheme.error) 
                }
            },
            dismissButton = { TextButton(onClick = { deleteId = null }) { Text("Cancel") } }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Journal") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToWrite(null) }, containerColor = Color(0xFF008080), contentColor = Color.White) { 
                Icon(Icons.Default.Add, "Write") 
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            OutlinedTextField(
                value = searchQuery, onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Search journals...") }, shape = MaterialTheme.shapes.large, singleLine = true
            )

            if (filteredJournals.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Start writing your thoughts...", color = Color.Gray, fontSize = 16.sp, textAlign = TextAlign.Center)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredJournals, key = { it.id }) { journal ->
                        JournalCard(journal, onClick = { onNavigateToDetail(journal.id) }, onDelete = { deleteId = journal.id })
                    }
                }
            }
        }
    }
}

@Composable
fun JournalCard(journal: Journal, onClick: () -> Unit, onDelete: () -> Unit) {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(journal.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(sdf.format(Date(journal.date)), color = Color.Gray, fontSize = 12.sp)
                    Text("•", color = Color.Gray, fontSize = 12.sp)
                    Text("${journal.wordCount} words", color = Color.Gray, fontSize = 12.sp)
                }
                if (!journal.aiEmotion.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    EmotionChip(journal.aiEmotion)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f))
            }
        }
    }
}
