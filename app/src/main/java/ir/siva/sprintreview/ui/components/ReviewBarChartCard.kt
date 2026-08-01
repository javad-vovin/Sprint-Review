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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ir.siva.sprintreview.data.model.ReviewRecord

@Composable
fun ReviewBarChartCard(
    records: List<ReviewRecord>,
    modifier: Modifier = Modifier
) {
    val totalR1 = records.sumOf { it.r1Count }
    val totalR2 = records.sumOf { it.r2Count }
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    // Accessible high-contrast colors for dark and light modes
    val r1Color = if (isDark) Color(0xFF34D399) else Color(0xFF059669)
    val r2Color = if (isDark) Color(0xFFFBBF24) else Color(0xFFD97706)

    Column(modifier = modifier) {
        SingleReviewBarChartCard(
            title = "R1 Reviews Distribution",
            badgeText = "Total R1: $totalR1",
            records = records,
            countSelector = { it.r1Count },
            barColor = r1Color,
            badgeColor = r1Color
        )

        SingleReviewBarChartCard(
            title = "R2 Reviews Distribution",
            badgeText = "Total R2: $totalR2",
            records = records,
            countSelector = { it.r2Count },
            barColor = r2Color,
            badgeColor = r2Color
        )
    }
}

@Composable
fun SingleReviewBarChartCard(
    title: String,
    badgeText: String,
    records: List<ReviewRecord>,
    countSelector: (ReviewRecord) -> Int,
    barColor: Color,
    badgeColor: Color = barColor,
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
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(barColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            if (records.isEmpty()) {
                Text(
                    text = "No review records available",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val maxCount = (records.maxOfOrNull { countSelector(it) } ?: 1).coerceAtLeast(1)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    records.forEach { record ->
                        val count = countSelector(record)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = record.memberName,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .widthIn(min = 76.dp, max = 110.dp)
                                    .weight(0.32f)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            val fraction = count.toFloat() / maxCount.toFloat()

                            Box(
                                modifier = Modifier
                                    .weight(0.58f)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                                    .border(
                                        BorderStroke(
                                            0.5.dp,
                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                        ),
                                        RoundedCornerShape(7.dp)
                                    )
                            ) {
                                if (count > 0) {
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth(fraction)
                                            .height(14.dp)
                                            .clip(RoundedCornerShape(7.dp))
                                            .background(barColor)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "$count",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.widthIn(min = 24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

