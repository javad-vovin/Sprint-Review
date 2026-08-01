package ir.siva.sprintreview.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.siva.sprintreview.data.model.ReviewRecord
import ir.siva.sprintreview.ui.theme.Reviewer1Color
import ir.siva.sprintreview.ui.theme.Reviewer2Color

@Composable
fun MemberReviewCard(
    record: ReviewRecord,
    onIncrementR1: () -> Unit,
    onDecrementR1: () -> Unit,
    onIncrementR2: () -> Unit,
    onDecrementR2: () -> Unit,
    isEditable: Boolean = true,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(shape)
    ) {
        // Frosted blur glass background
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(16.dp)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.75f))
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)), shape)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            MemberAvatar(
                memberName = record.memberName,
                avatarColorHex = record.avatarColorHex,
                size = 46.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Member info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.memberName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "Total: ${record.totalReviews}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // R1 Controls Box
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReviewCounterPill(
                    label = "R1",
                    count = record.r1Count,
                    badgeColor = Reviewer1Color,
                    isEditable = isEditable,
                    onDecrement = onDecrementR1,
                    onIncrement = onIncrementR1,
                    decTestTag = "btn_dec_r1_${record.id}",
                    incTestTag = "btn_inc_r1_${record.id}"
                )

                ReviewCounterPill(
                    label = "R2",
                    count = record.r2Count,
                    badgeColor = Reviewer2Color,
                    isEditable = isEditable,
                    onDecrement = onDecrementR2,
                    onIncrement = onIncrementR2,
                    decTestTag = "btn_dec_r2_${record.id}",
                    incTestTag = "btn_inc_r2_${record.id}"
                )
            }
        }
    }
}

@Composable
private fun ReviewCounterPill(
    label: String,
    count: Int,
    badgeColor: Color,
    isEditable: Boolean,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    decTestTag: String,
    incTestTag: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = badgeColor.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.3f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = badgeColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isEditable) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, badgeColor.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = onDecrement,
                            modifier = Modifier
                                .size(26.dp)
                                .testTag(decTestTag)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Dec $label",
                                tint = badgeColor,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "$count",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                if (isEditable) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(badgeColor),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = onIncrement,
                            modifier = Modifier
                                .size(26.dp)
                                .testTag(incTestTag)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Inc $label",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
