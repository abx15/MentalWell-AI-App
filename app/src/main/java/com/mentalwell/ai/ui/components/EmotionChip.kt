package com.mentalwell.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun EmotionChip(emotion: String?) {
    if (emotion.isNullOrBlank()) return

    val type = emotion.lowercase(Locale.getDefault())
    val (bgColor, textColor) = when (type) {
        "happy" -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "sad" -> Pair(Color(0xFFE3F2FD), Color(0xFF1565C0))
        "anxious" -> Pair(Color(0xFFF3E5F5), Color(0xFF6A1B9A))
        "angry" -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
        "hopeful" -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17))
        else -> Pair(Color(0xFFEEEEEE), Color(0xFF424242)) // Neutral Gray
    }

    Box(
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(bgColor).padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = emotion.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            fontSize = 12.sp, color = textColor, fontWeight = FontWeight.SemiBold
        )
    }
}
