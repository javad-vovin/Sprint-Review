package ir.siva.sprintreview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
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
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Review Distribution Chart",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (records.isEmpty()) {
                Text(
                    text = "No review records available for chart",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val maxTotal = (records.maxOfOrNull { it.totalReviews } ?: 1).coerceAtLeast(1)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    records.take(6).forEach { record ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = record.memberName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                modifier = Modifier.width(90.dp)
                            )

                            val r1 = record.r1Count
                            val r2 = record.r2Count
                            val total = r1 + r2
                            val remaining = (maxTotal - total).coerceAtLeast(0)

                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (r1 > 0) {
                                    Box(
                                        modifier = Modifier
                                            .weight(r1.toFloat())
                                            .height(14.dp)
                                            .background(Reviewer1Color)
                                    )
                                }
                                if (r2 > 0) {
                                    Box(
                                        modifier = Modifier
                                            .weight(r2.toFloat())
                                            .height(14.dp)
                                            .background(Reviewer2Color)
                                    )
                                }
                                if (remaining > 0) {
                                    Spacer(
                                        modifier = Modifier.weight(remaining.toFloat())
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$r1/$r2",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(36.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
