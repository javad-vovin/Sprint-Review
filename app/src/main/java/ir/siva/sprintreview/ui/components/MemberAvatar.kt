package ir.siva.sprintreview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun generateInitials(name: String): String {
    val parts = name.trim().split(" ")
    return when {
        parts.size >= 2 -> "${parts[0].take(1)}${parts[1].take(1)}".uppercase()
        parts.isNotEmpty() && parts[0].isNotEmpty() -> parts[0].take(2).uppercase()
        else -> "??"
    }
}

fun generateUniqueAvatarColor(name: String): String {
    val colors = listOf(
        "#38BDF8", "#F43F5E", "#10B981", "#F59E0B", "#8B5CF6",
        "#EC4899", "#06B6D4", "#84CC16", "#6366F1", "#14B8A6"
    )
    val hash = name.hashCode().let { if (it < 0) -it else it }
    return colors[hash % colors.size]
}

@Composable
fun MemberAvatar(
    memberName: String = "",
    name: String = memberName,
    avatarColorHex: String = "",
    colorHex: String = if (avatarColorHex.isNotEmpty()) avatarColorHex else generateUniqueAvatarColor(name),
    size: Dp = 40.dp,
    fontSize: TextUnit = (size.value * 0.38).sp,
    modifier: Modifier = Modifier
) {
    val displayName = if (name.isNotEmpty()) name else memberName
    val hex = if (colorHex.isNotEmpty()) colorHex else generateUniqueAvatarColor(displayName)

    val color = try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = generateInitials(displayName),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize
        )
    }
}
