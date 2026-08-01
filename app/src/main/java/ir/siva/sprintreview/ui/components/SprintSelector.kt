package ir.siva.sprintreview.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.siva.sprintreview.data.model.Sprint

@Composable
fun SprintSelector(
    sprints: List<Sprint>,
    selectedSprint: Sprint?,
    onSprintSelected: (Sprint) -> Unit,
    onAddSprintClicked: () -> Unit = {},
    sprintStartDay: String = "Saturday",
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
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = selectedSprint?.name ?: "Sprint 1",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
