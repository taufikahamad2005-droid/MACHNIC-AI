package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DiagnosticRecord
import com.example.data.local.TechnicianProfile
import com.example.data.local.UserPreferences
import com.example.data.model.DiagnosticResult
import com.example.data.model.LibraryCategory
import com.example.data.model.RepairGuide
import com.example.data.model.VehicleInput
import com.example.data.remote.GeminiApiService
import com.example.data.repository.DiagnosticRepository
import com.example.data.repository.LibraryRepository
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.localization.EnglishStrings
import com.example.ui.localization.HindiStrings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    data object Welcome : Screen()
    data object Auth : Screen()
    data object Dashboard : Screen()
    data object Search : Screen()
    data class Result(val result: DiagnosticResult, val recordId: Long) : Screen()
    data object History : Screen()
    data object Library : Screen()
    data class GuideDetail(val guide: RepairGuide) : Screen()
    data object Profile : Screen()
    data object InstallationGuide : Screen()
    data object About : Screen()
}

class MechanicViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)
    private val database = AppDatabase.getDatabase(application)
    private val apiService = GeminiApiService.create()
    private val repository = DiagnosticRepository(database.diagnosticDao(), apiService)

    // Navigation Stack / Current Screen
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Dashboard)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf<Screen>(Screen.Dashboard)

    fun navigateTo(screen: Screen) {
        screenStack.add(screen)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = screenStack.last()
            return true
        }
        return false
    }

    // Language
    val currentLanguage: StateFlow<AppLanguage> = userPreferences.selectedLanguage.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppLanguage.ENGLISH
    )

    fun toggleLanguage() {
        viewModelScope.launch {
            val next = if (currentLanguage.value == AppLanguage.ENGLISH) AppLanguage.HINDI else AppLanguage.ENGLISH
            userPreferences.setLanguage(next)
        }
    }

    fun setLanguage(lang: AppLanguage) {
        viewModelScope.launch {
            userPreferences.setLanguage(lang)
        }
    }

    fun getStrings(): AppStrings {
        return if (currentLanguage.value == AppLanguage.HINDI) HindiStrings else EnglishStrings
    }

    // Profile & Auth
    val profile: StateFlow<TechnicianProfile> = userPreferences.profile.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TechnicianProfile(
            name = "Senior Diagnostic Specialist",
            garageName = "Apex Auto Workshop",
            experienceLevel = "Diagnostic Specialist",
            email = "tech@mechanicai.local",
            phone = "+91 98765 43210",
            isLoggedIn = true
        )
    )

    fun updateProfile(name: String, garage: String, level: String, email: String, phone: String) {
        viewModelScope.launch {
            userPreferences.updateProfile(name, garage, level, email, phone)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.setLoggedIn(false)
            _currentScreen.value = Screen.Auth
        }
    }

    fun login(email: String, name: String) {
        viewModelScope.launch {
            userPreferences.updateProfile(
                name = name.ifBlank { "Automotive Technician" },
                garageName = profile.value.garageName,
                experienceLevel = profile.value.experienceLevel,
                email = email.ifBlank { "mechanic@garage.local" },
                phone = profile.value.phone
            )
            userPreferences.setLoggedIn(true)
            navigateTo(Screen.Dashboard)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllRecords()
            userPreferences.clearAllData()
            _currentScreen.value = Screen.Welcome
        }
    }

    // Diagnostics / Search
    private val _isDiagnosing = MutableStateFlow(false)
    val isDiagnosing: StateFlow<Boolean> = _isDiagnosing.asStateFlow()

    private val _diagnosisError = MutableStateFlow<String?>(null)
    val diagnosisError: StateFlow<String?> = _diagnosisError.asStateFlow()

    fun runDiagnostic(input: VehicleInput) {
        viewModelScope.launch {
            _isDiagnosing.value = true
            _diagnosisError.value = null
            try {
                val customKey = null
                val (result, recordId) = repository.runDiagnosis(input, customKey)
                _isDiagnosing.value = false
                navigateTo(Screen.Result(result, recordId))
            } catch (e: Exception) {
                _isDiagnosing.value = false
                _diagnosisError.value = e.localizedMessage ?: "Diagnosis failed. Please check network."
            }
        }
    }

    fun reopenResult(record: DiagnosticRecord) {
        val result = repository.parseFromJson(record.fullResultJson, record.faultCode)
        navigateTo(Screen.Result(result, record.id))
    }

    // History & Search Filter
    private val _historySearchQuery = MutableStateFlow("")
    val historySearchQuery: StateFlow<String> = _historySearchQuery.asStateFlow()

    fun setHistorySearchQuery(query: String) {
        _historySearchQuery.value = query
    }

    val allHistoryRecords: StateFlow<List<DiagnosticRecord>> = repository.allRecords.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val recentRecords: StateFlow<List<DiagnosticRecord>> = repository.getRecentRecords(5).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val bookmarkedRecords: StateFlow<List<DiagnosticRecord>> = repository.bookmarkedRecords.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val filteredRecords: StateFlow<List<DiagnosticRecord>> = combine(
        allHistoryRecords,
        _historySearchQuery
    ) { records, query ->
        if (query.isBlank()) {
            records
        } else {
            val q = query.trim().lowercase()
            records.filter {
                it.faultCode.lowercase().contains(q) ||
                it.vehicleMake.lowercase().contains(q) ||
                it.vehicleModel.lowercase().contains(q) ||
                it.diagnosticTitle.lowercase().contains(q)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun deleteRecord(id: Long) {
        viewModelScope.launch {
            repository.deleteRecord(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearAllRecords()
        }
    }

    fun toggleBookmark(id: Long, current: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(id, current)
        }
    }

    fun updateRepairNotes(id: Long, notes: String) {
        viewModelScope.launch {
            repository.updateRepairNotes(id, notes)
        }
    }

    // Library
    val libraryCategories: List<LibraryCategory> = LibraryRepository.categories

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _librarySearchQuery = MutableStateFlow("")
    val librarySearchQuery: StateFlow<String> = _librarySearchQuery.asStateFlow()

    fun selectCategory(catId: String?) {
        _selectedCategory.value = catId
    }

    fun setLibrarySearchQuery(query: String) {
        _librarySearchQuery.value = query
    }

    fun getFilteredGuides(): List<RepairGuide> {
        val q = _librarySearchQuery.value
        val cat = _selectedCategory.value
        val base = if (cat != null) LibraryRepository.getGuidesByCategory(cat) else LibraryRepository.guides
        return if (q.isBlank()) {
            base
        } else {
            val query = q.trim().lowercase()
            base.filter {
                it.title.lowercase().contains(query) ||
                it.titleHi.lowercase().contains(query) ||
                it.summary.lowercase().contains(query) ||
                it.categoryName.lowercase().contains(query)
            }
        }
    }
}
