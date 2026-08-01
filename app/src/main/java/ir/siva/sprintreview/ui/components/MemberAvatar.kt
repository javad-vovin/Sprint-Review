package ir.siva.sprintreview.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.siva.sprintreview.R

data class AvatarOption(
    val id: String,
    val title: String,
    val drawableRes: Int
)

val AVATAR_OPTIONS = listOf(
    AvatarOption("ic_avatar_male_dev1", "Male Dev 1", R.drawable.ic_avatar_male_dev1),
    AvatarOption("ic_avatar_female_dev1", "Female Dev 1", R.drawable.ic_avatar_female_dev1),
    AvatarOption("ic_avatar_male_dev2", "Male Dev 2", R.drawable.ic_avatar_male_dev2),
    AvatarOption("ic_avatar_female_dev2", "Female Dev 2", R.drawable.ic_avatar_female_dev2),
    AvatarOption("ic_avatar_mobile_dev", "Mobile Dev", R.drawable.ic_avatar_mobile_dev),
    AvatarOption("ic_avatar_tech_lead", "Tech Lead", R.drawable.ic_avatar_tech_lead),
    AvatarOption("ic_avatar_devops", "DevOps", R.drawable.ic_avatar_devops),
    AvatarOption("ic_avatar_qa_engineer", "QA Engineer", R.drawable.ic_avatar_qa_engineer)
)

fun getAvatarDrawableRes(avatarKeyOrColor: String, memberName: String): Int {
    val found = AVATAR_OPTIONS.find {
        it.id.equals(avatarKeyOrColor, ignoreCase = true) ||
        avatarKeyOrColor.contains(it.id, ignoreCase = true)
    }
    if (found != null) return found.drawableRes

    val hash = kotlin.math.abs((memberName + avatarKeyOrColor).hashCode())
    return AVATAR_OPTIONS[hash % AVATAR_OPTIONS.size].drawableRes
}

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
    colorHex: String = avatarColorHex,
    size: Dp = 40.dp,
    fontSize: TextUnit = (size.value * 0.38).sp,
    modifier: Modifier = Modifier
) {
    val displayName = if (name.isNotEmpty()) name else memberName
    val key = if (avatarColorHex.isNotEmpty()) avatarColorHex else colorHex
    val drawableRes = getAvatarDrawableRes(key, displayName)

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = displayName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
