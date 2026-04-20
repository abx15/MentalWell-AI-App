package com.mentalwell.ai.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "Typing Transition System")
    
    val dot1Scale by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(400, 0, LinearEasing), RepeatMode.Reverse), label = "dot1"
    )
    val dot2Scale by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(400, 150, LinearEasing), RepeatMode.Reverse), label = "dot2"
    )
    val dot3Scale by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(400, 300, LinearEasing), RepeatMode.Reverse), label = "dot3"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        val themeColor = MaterialTheme.colorScheme.onSurfaceVariant
        Box(modifier = Modifier.size(6.dp).scale(dot1Scale).background(themeColor, CircleShape))
        Box(modifier = Modifier.size(6.dp).scale(dot2Scale).background(themeColor, CircleShape))
        Box(modifier = Modifier.size(6.dp).scale(dot3Scale).background(themeColor, CircleShape))
    }
}
