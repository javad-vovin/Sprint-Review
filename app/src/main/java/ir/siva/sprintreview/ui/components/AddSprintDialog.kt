package ir.siva.sprintreview.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.siva.sprintreview.data.model.Sprint

@Composable
fun AddSprintDialog(
    latestSprint: Sprint?,
    onDismiss: () -> Unit,
    onConfirm: (name: String, startDate: Long, endDate: Long, number: Int) -> Unit
) {
    val nextNumber = (latestSprint?.sprintNumber ?: 0) + 1
    var sprintName by remember { mutableStateOf("Sprint $nextNumber") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Create New Sprint", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = sprintName,
                    onValueChange = { sprintName = it },
                    label = { Text("Sprint Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_sprint_name")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (sprintName.isNotBlank()) {
                        val now = System.currentTimeMillis()
                        val duration = 14 * 24 * 60 * 60 * 1000L
                        onConfirm(sprintName.trim(), now, now + duration, nextNumber)
                        onDismiss()
                    }
                },
                enabled = sprintName.isNotBlank(),
                modifier = Modifier.testTag("btn_confirm_add_sprint")
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
