package com.mentalwell.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mentalwell.ai.data.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatBubble(message: ChatMessage) {
    var showTime by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .pointerInput(message.id) {
                detectTapGestures(onLongPress = { showTime = !showTime })
            },
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        val bubbleShape = if (message.isUser) {
            RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
        } else {
            RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
        }

        val backgroundColor = if (message.isUser) Color(0xFF008080) else MaterialTheme.colorScheme.surfaceVariant
        val textColor = if (message.isUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

        Surface(
            shape = bubbleShape, color = backgroundColor,
            shadowElevation = 1.dp, modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                if (message.isLoading) TypingIndicator()
                else Text(text = message.text, color = textColor, fontSize = 15.sp)
            }
        }

        if (!message.isUser && !message.isLoading && !message.emotionHint.isNullOrEmpty() && message.emotionHint != "neutral") {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFF4DB6AC).copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("Detected: ${message.emotionHint}", fontSize = 11.sp, color = Color(0xFF008080), fontWeight = FontWeight.Medium)
            }
        }

        if (showTime && !message.isLoading) {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            Text(sdf.format(Date(message.timestamp)), fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(top = 2.dp))
        }
    }
}
