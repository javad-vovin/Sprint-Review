package ir.siva.sprintreview.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.siva.sprintreview.data.model.ReviewRecord
import ir.siva.sprintreview.ui.theme.Reviewer1Color
import ir.siva.sprintreview.ui.theme.Reviewer2Color

@Composable
fun ReviewBarChartCard(
    records: List<ReviewRecord>,
    modifier: Modifier = Modifier
) {
    val totalR1 = records.sumOf { it.r1Count }
    val totalR2 = records.sumOf { it.r2Count }

    Column(modifier = modifier) {
        SingleReviewBarChartCard(
            title = "R1 Reviews Distribution",
            badgeText = "Total R1: $totalR1",
            records = records,
            countSelector = { it.r1Count },
            barColor = Reviewer1Color
        )
        SingleReviewBarChartCard(
            title = "R2 Reviews Distribution",
            badgeText = "Total R2: $totalR2",
            records = records,
            countSelector = { it.r2Count },
            barColor = Reviewer2Color
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
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
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = badgeText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = barColor
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (records.isEmpty()) {
                Text(
                    text = "No review records available",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val maxCount = (records.maxOfOrNull { countSelector(it) } ?: 1).coerceAtLeast(1)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                modifier = Modifier.width(90.dp)
                            )

                            val fraction = count.toFloat() / maxCount.toFloat()

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
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
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$count",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
