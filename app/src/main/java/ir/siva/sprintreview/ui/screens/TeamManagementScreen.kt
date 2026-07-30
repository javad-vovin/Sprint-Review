package ir.siva.sprintreview.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.siva.sprintreview.data.model.ReviewRecord
import ir.siva.sprintreview.ui.components.AddMemberDialog
import ir.siva.sprintreview.ui.components.EditMemberDialog
import ir.siva.sprintreview.ui.components.MemberAvatar
import ir.siva.sprintreview.ui.theme.Reviewer1Color
import ir.siva.sprintreview.ui.theme.Reviewer2Color
import ir.siva.sprintreview.ui.viewmodel.CodeReviewViewModel
import ir.siva.sprintreview.ui.viewmodel.DashboardUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamManagementScreen(
    viewModel: CodeReviewViewModel,
    uiState: DashboardUiState,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var memberToEdit by remember { mutableStateOf<ReviewRecord?>(null) }
    var memberToDelete by remember { mutableStateOf<ReviewRecord?>(null) }

    val filteredRecords = uiState.records.filter {
        searchQuery.isBlank() || it.memberName.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 12.dp,
                bottom = paddingValues.calculateBottomPadding() + 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Overview Summary Card
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("team_summary_card"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${uiState.records.size} Team Members",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Active reviewers in ${uiState.selectedSprint?.name ?: "Current Sprint"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { showAddDialog = true },
                            modifier = Modifier.testTag("btn_card_add_member")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add")
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_search_team"),
                    placeholder = { Text("Search member name...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Team Members List
            items(filteredRecords, key = { it.id }) { record ->
                TeamMemberManageRow(
                    record = record,
                    onEditClicked = { memberToEdit = record },
                    onDeleteClicked = { memberToDelete = record }
                )
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    // Add Member Dialog
    if (showAddDialog) {
        AddMemberDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, colorHex ->
                uiState.selectedSprint?.let { sprint ->
                    viewModel.addMemberToSprint(sprint.id, name, colorHex)
                }
            }
        )
    }

    // Edit Member Dialog
    memberToEdit?.let { record ->
        EditMemberDialog(
            record = record,
            onDismiss = { memberToEdit = null },
            onConfirm = { newName, newColorHex ->
                viewModel.updateMemberRecord(
                    record.copy(memberName = newName, avatarColorHex = newColorHex)
                )
            }
        )
    }

    // Delete Member Dialog
    memberToDelete?.let { record ->
        AlertDialog(
            onDismissRequest = { memberToDelete = null },
            title = { Text("Delete Team Member", fontWeight = FontWeight.Bold) },
            text = {
                Text("Are you sure you want to remove ${record.memberName} from ${uiState.selectedSprint?.name ?: "this sprint"}? Review history will be kept in database.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteMemberRecord(record)
                        memberToDelete = null
                    },
                    modifier = Modifier.testTag("btn_confirm_delete_team_member")
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { memberToDelete = null },
                    modifier = Modifier.testTag("btn_cancel_delete_team_member")
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun TeamMemberManageRow(
    record: ReviewRecord,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("row_member_${record.id}"),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Unique Initials Avatar
            MemberAvatar(
                name = record.memberName,
                colorHex = record.avatarColorHex,
                size = 44.dp,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Member Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.memberName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "R1: ${record.r1Count}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Reviewer1Color
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "R2: ${record.r2Count}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Reviewer2Color
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Total: ${record.totalReviews}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Edit & Delete Actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onEditClicked,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("btn_row_edit_${record.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Member",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDeleteClicked,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("btn_row_delete_${record.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Member",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
