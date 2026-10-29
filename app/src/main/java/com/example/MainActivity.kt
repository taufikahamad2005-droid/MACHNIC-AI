package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.VehicleInput
import com.example.ui.MechanicViewModel
import com.example.ui.Screen
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppTopBar
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DiagnosticResultScreen
import com.example.ui.screens.GuideDetailScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.InstallationScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MechanicAITheme
import com.example.ui.theme.NavyDarkBackground

class MainActivity : ComponentActivity() {

    private val viewModel: MechanicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MechanicAITheme {
                MechanicApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MechanicApp(viewModel: MechanicViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val strings = viewModel.getStrings()

    val recentRecords by viewModel.recentRecords.collectAsStateWithLifecycle()
    val allHistory by viewModel.filteredRecords.collectAsStateWithLifecycle()
    val bookmarkedRecords by viewModel.bookmarkedRecords.collectAsStateWithLifecycle()
    val historySearchQuery by viewModel.historySearchQuery.collectAsStateWithLifecycle()

    val isDiagnosing by viewModel.isDiagnosing.collectAsStateWithLifecycle()
    val diagnosisError by viewModel.diagnosisError.collectAsStateWithLifecycle()

    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val librarySearchQuery by viewModel.librarySearchQuery.collectAsStateWithLifecycle()
    val guides = viewModel.getFilteredGuides()

    // Back handling for navigation stack
    val isRootScreen = currentScreen is Screen.Dashboard || currentScreen is Screen.Welcome || currentScreen is Screen.Auth
    BackHandler(enabled = !isRootScreen) {
        viewModel.navigateBack()
    }

    val showTopBar = currentScreen !is Screen.Welcome && currentScreen !is Screen.Auth
    val showBottomNav = currentScreen is Screen.Dashboard ||
            currentScreen is Screen.Search ||
            currentScreen is Screen.History ||
            currentScreen is Screen.Library ||
            currentScreen is Screen.Profile

    val topBarTitle = when (currentScreen) {
        is Screen.Dashboard -> strings.appName
        is Screen.Search -> strings.diagnose
        is Screen.Result -> strings.diagnosticResult
        is Screen.History -> strings.history
        is Screen.Library -> strings.library
        is Screen.GuideDetail -> strings.library
        is Screen.Profile -> strings.profile
        is Screen.InstallationGuide -> strings.installApp
        is Screen.About -> strings.aboutSafety
        else -> strings.appName
    }

    val topBarSubtitle = when (currentScreen) {
        is Screen.Dashboard -> strings.tagline
        is Screen.Search -> "OBD-II Fault Code & Live Telemetry"
        is Screen.Result -> "Automotive Diagnostic Report"
        is Screen.History -> "Stored Diagnostic Scans"
        is Screen.Library -> "15 Technical Repair Disciplines"
        is Screen.GuideDetail -> "Technical Specification"
        is Screen.Profile -> profile.garageName
        is Screen.InstallationGuide -> "Android Phone Deployment"
        is Screen.About -> "Safety & Compliance Standards"
        else -> null
    }

    Scaffold(
        topBar = {
            if (showTopBar) {
                AppTopBar(
                    title = topBarTitle,
                    subtitle = topBarSubtitle,
                    showBackButton = !isRootScreen,
                    onBackClick = { viewModel.navigateBack() },
                    currentLanguage = currentLanguage,
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    modifier = Modifier.statusBarsPadding()
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                AppBottomNav(
                    currentScreen = currentScreen,
                    strings = strings,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        },
        containerColor = NavyDarkBackground
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = NavyDarkBackground
        ) {
            when (val screen = currentScreen) {
                is Screen.Welcome -> {
                    WelcomeScreen(
                        strings = strings,
                        onGetStarted = { viewModel.navigateTo(Screen.Dashboard) },
                        onLoginClick = { viewModel.navigateTo(Screen.Auth) }
                    )
                }

                is Screen.Auth -> {
                    AuthScreen(
                        strings = strings,
                        onLoginSuccess = { email, name ->
                            viewModel.login(email, name)
                        },
                        onSkip = { viewModel.navigateTo(Screen.Dashboard) }
                    )
                }

                is Screen.Dashboard -> {
                    DashboardScreen(
                        strings = strings,
                        profile = profile,
                        recentRecords = recentRecords,
                        categories = viewModel.libraryCategories,
                        currentLanguage = currentLanguage,
                        onQuickSearch = { code ->
                            viewModel.runDiagnostic(VehicleInput(faultCode = code))
                        },
                        onOpenSearch = { viewModel.navigateTo(Screen.Search) },
                        onReopenRecord = { record -> viewModel.reopenResult(record) },
                        onOpenCategory = { catId ->
                            viewModel.selectCategory(catId)
                            viewModel.navigateTo(Screen.Library)
                        },
                        onOpenHistory = { viewModel.navigateTo(Screen.History) },
                        onOpenLibrary = { viewModel.navigateTo(Screen.Library) }
                    )
                }

                is Screen.Search -> {
                    SearchScreen(
                        strings = strings,
                        currentLanguage = currentLanguage,
                        isDiagnosing = isDiagnosing,
                        diagnosisError = diagnosisError,
                        onRunDiagnostic = { input ->
                            viewModel.runDiagnostic(input)
                        }
                    )
                }

                is Screen.Result -> {
                    DiagnosticResultScreen(
                        result = screen.result,
                        recordId = screen.recordId,
                        strings = strings,
                        currentLanguage = currentLanguage,
                        onToggleBookmark = { id, current ->
                            viewModel.toggleBookmark(id, current)
                        },
                        onSaveNotes = { id, notes ->
                            viewModel.updateRepairNotes(id, notes)
                        }
                    )
                }

                is Screen.History -> {
                    HistoryScreen(
                        records = allHistory,
                        bookmarkedRecords = bookmarkedRecords,
                        searchQuery = historySearchQuery,
                        onSearchQueryChange = { q -> viewModel.setHistorySearchQuery(q) },
                        strings = strings,
                        currentLanguage = currentLanguage,
                        onReopenRecord = { record -> viewModel.reopenResult(record) },
                        onDeleteRecord = { id -> viewModel.deleteRecord(id) },
                        onToggleBookmark = { id, current -> viewModel.toggleBookmark(id, current) },
                        onClearAllHistory = { viewModel.clearAllHistory() }
                    )
                }

                is Screen.Library -> {
                    LibraryScreen(
                        categories = viewModel.libraryCategories,
                        selectedCategory = selectedCategory,
                        onSelectCategory = { catId -> viewModel.selectCategory(catId) },
                        searchQuery = librarySearchQuery,
                        onSearchQueryChange = { q -> viewModel.setLibrarySearchQuery(q) },
                        guides = guides,
                        strings = strings,
                        currentLanguage = currentLanguage,
                        onOpenGuide = { guide -> viewModel.navigateTo(Screen.GuideDetail(guide)) }
                    )
                }

                is Screen.GuideDetail -> {
                    GuideDetailScreen(
                        guide = screen.guide,
                        strings = strings,
                        currentLanguage = currentLanguage
                    )
                }

                is Screen.Profile -> {
                    ProfileScreen(
                        profile = profile,
                        strings = strings,
                        currentLanguage = currentLanguage,
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onUpdateProfile = { name, garage, level, email, phone ->
                            viewModel.updateProfile(name, garage, level, email, phone)
                        },
                        onOpenInstallation = { viewModel.navigateTo(Screen.InstallationGuide) },
                        onOpenAbout = { viewModel.navigateTo(Screen.About) },
                        onLogout = { viewModel.logout() },
                        onClearAllData = { viewModel.clearAllData() }
                    )
                }

                is Screen.InstallationGuide -> {
                    InstallationScreen(
                        strings = strings,
                        currentLanguage = currentLanguage
                    )
                }

                is Screen.About -> {
                    AboutScreen(
                        strings = strings,
                        currentLanguage = currentLanguage
                    )
                }
            }
        }
    }
}
