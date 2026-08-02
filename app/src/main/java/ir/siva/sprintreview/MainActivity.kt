package ir.siva.sprintreview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.siva.sprintreview.data.database.AppDatabase
import ir.siva.sprintreview.data.repository.CodeReviewRepository
import ir.siva.sprintreview.ui.components.AbstractGradientBackground
import ir.siva.sprintreview.ui.components.FloatingBottomNavigation
import ir.siva.sprintreview.ui.screens.MainDashboardScreen
import ir.siva.sprintreview.ui.screens.SettingsScreen
import ir.siva.sprintreview.ui.screens.StatisticsScreen
import ir.siva.sprintreview.ui.screens.TeamManagementScreen
import ir.siva.sprintreview.ui.theme.SprintReviewTheme
import ir.siva.sprintreview.ui.theme.ThemeMode
import ir.siva.sprintreview.ui.viewmodel.CodeReviewViewModel
import ir.siva.sprintreview.ui.viewmodel.CodeReviewViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val savedThemeModeName = prefs.getString("theme_mode", ThemeMode.SYSTEM.name)
        val initialThemeMode = try {
            ThemeMode.valueOf(savedThemeModeName ?: ThemeMode.SYSTEM.name)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CodeReviewRepository(database.sprintDao(), database.reviewRecordDao())
        val factory = CodeReviewViewModelFactory(repository)

        setContent {
            var themeMode by remember { mutableStateOf(initialThemeMode) }
            var currentRoute by remember { mutableStateOf("dashboard") }

            SprintReviewTheme(themeMode = themeMode) {
                val viewModel: CodeReviewViewModel = viewModel(factory = factory)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                AbstractGradientBackground {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                    when (currentRoute) {
                        "dashboard" -> MainDashboardScreen(
                            viewModel = viewModel,
                            uiState = uiState,
                            onNavigateToTeamManagement = { currentRoute = "team" }
                        )
                        "statistics" -> StatisticsScreen(
                            viewModel = viewModel,
                            uiState = uiState
                        )
                        "team" -> TeamManagementScreen(
                            viewModel = viewModel,
                            uiState = uiState,
                            onNavigateBack = { currentRoute = "dashboard" }
                        )
                        "settings" -> SettingsScreen(
                            viewModel = viewModel,
                            uiState = uiState,
                            themeMode = themeMode,
                            onThemeModeChanged = { newMode ->
                                themeMode = newMode
                                prefs.edit().putString("theme_mode", newMode.name).apply()
                            }
                        )
                    }

                    FloatingBottomNavigation(
                        currentRoute = currentRoute,
                        onNavigate = { route -> currentRoute = route },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .windowInsetsPadding(WindowInsets.navigationBars)
                    )
                }
            }
            }
        }
    }
}
