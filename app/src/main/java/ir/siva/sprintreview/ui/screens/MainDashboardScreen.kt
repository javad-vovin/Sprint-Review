package ir.siva.sprintreview.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import ir.siva.sprintreview.ui.components.AddMemberDialog
import ir.siva.sprintreview.ui.components.AddSprintDialog
import ir.siva.sprintreview.ui.components.EditMemberDialog
import ir.siva.sprintreview.ui.components.MemberReviewCard
import ir.siva.sprintreview.ui.components.ReviewBarChartCard
import ir.siva.sprintreview.ui.components.SprintSelector
import ir.siva.sprintreview.ui.components.SprintSettingsDialog
import ir.siva.sprintreview.ui.components.SprintSummaryCard
import ir.siva.sprintreview.ui.viewmodel.CodeReviewViewModel
import ir.siva.sprintreview.ui.viewmodel.DashboardUiState
import ir.siva.sprintreview.ui.viewmodel.MemberSortFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardScreen(
    viewModel: CodeReviewViewModel,
    uiState: DashboardUiState,
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onNavigateToTeamManagement: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var showAddSprintDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var memberToEdit by remember { mutableStateOf<ir.siva.sprintreview.data.model.ReviewRecord?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 12.dp,
                    bottom = innerPadding.calculateBottomPadding() + 100.dp
                )
            ) {
                // Sprint Switcher Header (Current Sprint vs Previous Sprint selector)
                item {
                    SprintSelector(
                        sprints = uiState.sprints,
                        selectedSprint = uiState.selectedSprint,
                        onSprintSelected = { sprint ->
                            viewModel.selectSprint(sprint.id)
                        },
                        onAddSprintClicked = {
                            showAddSprintDialog = true
                        },
                        sprintStartDay = uiState.sprintStartDay
                    )
                }

                // Simple R1 & R2 Chart
                item {
                    ReviewBarChartCard(
                        records = uiState.records
                    )
                }

                // Team Members Section Title
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Team Member Review Counts (${uiState.filteredRecords.size})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "R1: Reviewer 1 | R2: Reviewer 2",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        TextButton(
                            onClick = onNavigateToTeamManagement,
                            modifier = Modifier.testTag("btn_manage_team_members_header")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Manage Team", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Member Review Cards List
                if (uiState.filteredRecords.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "No team members found",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tap 'Add Member' to add team members to this sprint.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(
                        items = uiState.filteredRecords,
                        key = { it.id }
                    ) { record ->
                        MemberReviewCard(
                            record = record,
                            onIncrementR1 = { viewModel.incrementR1(record.id) },
                            onDecrementR1 = { viewModel.decrementR1(record.id) },
                            onIncrementR2 = { viewModel.incrementR2(record.id) },
                            onDecrementR2 = { viewModel.decrementR2(record.id) },
                            isEditable = uiState.selectedSprint?.isCurrent == true
                        )
                    }
                }
            }
        }
    }

    // Dialogs
    if (showAddMemberDialog) {
        AddMemberDialog(
            onDismiss = { showAddMemberDialog = false },
            onConfirm = { name, colorHex ->
                viewModel.addMemberToCurrentSprint(name, colorHex)
            }
        )
    }

    if (showAddSprintDialog) {
        AddSprintDialog(
            latestSprint = uiState.sprints.firstOrNull(),
            onDismiss = { showAddSprintDialog = false },
            onConfirm = { name, startDate, endDate, number ->
                viewModel.createNewSprint(name, startDate, endDate, number)
            }
        )
    }

    if (showSettingsDialog) {
        SprintSettingsDialog(
            currentSprintName = uiState.selectedSprint?.name,
            currentStartDay = uiState.sprintStartDay,
            currentDurationWeeks = uiState.sprintDurationWeeks,
            onDismiss = { showSettingsDialog = false },
            onSaveSettings = { startDay, durationWeeks ->
                viewModel.updateSprintSettings(startDay, durationWeeks)
            },
            onRenameSprint = { newName ->
                uiState.selectedSprint?.let { sprint ->
                    viewModel.updateSprintName(sprint.id, newName)
                }
            }
        )
    }

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
}
