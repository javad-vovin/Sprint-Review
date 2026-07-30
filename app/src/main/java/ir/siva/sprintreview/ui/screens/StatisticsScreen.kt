package ir.siva.sprintreview.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.siva.sprintreview.ui.components.AddSprintDialog
import ir.siva.sprintreview.ui.components.ReviewBarChartCard
import ir.siva.sprintreview.ui.components.SprintSelector
import ir.siva.sprintreview.ui.components.SprintSummaryCard
import ir.siva.sprintreview.ui.viewmodel.CodeReviewViewModel
import ir.siva.sprintreview.ui.viewmodel.DashboardUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: CodeReviewViewModel,
    uiState: DashboardUiState,
    modifier: Modifier = Modifier
) {
    var showAddSprintDialog by remember { mutableStateOf(false) }

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
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // Sprint Cycle Selector Dropdown
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

                // Sprint Analytics Summary & Comparison Card
                item {
                    SprintSummaryCard(
                        analytics = uiState.analytics,
                        currentSprintName = uiState.selectedSprint?.name ?: "Current Sprint",
                        previousSprintName = uiState.previousSprint?.name
                    )
                }

                // Team Member Review Distribution Bar Chart
                item {
                    ReviewBarChartCard(
                        records = uiState.records
                    )
                }
            }
        }
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
}
