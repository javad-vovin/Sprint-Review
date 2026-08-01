package ir.siva.sprintreview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MemberAvatar(
                memberName = record.memberName,
                avatarColorHex = record.avatarColorHex,
                size = 42.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.memberName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total Reviews: ${record.totalReviews}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // R1 Controls
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("R1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Reviewer1Color)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isEditable) {
                        IconButton(
                            onClick = onDecrementR1,
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_dec_r1_${record.id}")
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Dec R1", modifier = Modifier.size(16.dp))
                        }
                    }
                    Text(
                        text = "${record.r1Count}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    if (isEditable) {
                        IconButton(
                            onClick = onIncrementR1,
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_inc_r1_${record.id}")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Inc R1", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // R2 Controls
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("R2", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Reviewer2Color)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isEditable) {
                        IconButton(
                            onClick = onDecrementR2,
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_dec_r2_${record.id}")
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Dec R2", modifier = Modifier.size(16.dp))
                        }
                    }
                    Text(
                        text = "${record.r2Count}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    if (isEditable) {
                        IconButton(
                            onClick = onIncrementR2,
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_inc_r2_${record.id}")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Inc R2", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
