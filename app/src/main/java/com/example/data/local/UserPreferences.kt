package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ui.localization.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "mechanic_ai_preferences")

data class TechnicianProfile(
    val name: String,
    val garageName: String,
    val experienceLevel: String,
    val email: String,
    val phone: String,
    val isLoggedIn: Boolean
)

class UserPreferences(private val context: Context) {

    companion object {
        val KEY_LANGUAGE = stringPreferencesKey("app_language")
        val KEY_USER_NAME = stringPreferencesKey("user_name")
        val KEY_GARAGE_NAME = stringPreferencesKey("garage_name")
        val KEY_EXPERIENCE_LEVEL = stringPreferencesKey("experience_level")
        val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        val KEY_USER_PHONE = stringPreferencesKey("user_phone")
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val KEY_CUSTOM_API_KEY = stringPreferencesKey("custom_gemini_api_key")
    }

    val selectedLanguage: Flow<AppLanguage> = context.dataStore.data.map { prefs ->
        val code = prefs[KEY_LANGUAGE] ?: AppLanguage.ENGLISH.code
        if (code == AppLanguage.HINDI.code) AppLanguage.HINDI else AppLanguage.ENGLISH
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language.code
        }
    }

    val profile: Flow<TechnicianProfile> = context.dataStore.data.map { prefs ->
        TechnicianProfile(
            name = prefs[KEY_USER_NAME] ?: "Master Technician",
            garageName = prefs[KEY_GARAGE_NAME] ?: "Apex Auto Diagnostics",
            experienceLevel = prefs[KEY_EXPERIENCE_LEVEL] ?: "Diagnostic Specialist",
            email = prefs[KEY_USER_EMAIL] ?: "tech@mechanicai.local",
            phone = prefs[KEY_USER_PHONE] ?: "+91 98765 43210",
            isLoggedIn = prefs[KEY_IS_LOGGED_IN] ?: true
        )
    }

    suspend fun updateProfile(
        name: String,
        garageName: String,
        experienceLevel: String,
        email: String,
        phone: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_NAME] = name
            prefs[KEY_GARAGE_NAME] = garageName
            prefs[KEY_EXPERIENCE_LEVEL] = experienceLevel
            prefs[KEY_USER_EMAIL] = email
            prefs[KEY_USER_PHONE] = phone
            prefs[KEY_IS_LOGGED_IN] = true
        }
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = isLoggedIn
        }
    }

    val customApiKey: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_CUSTOM_API_KEY]
    }

    suspend fun setCustomApiKey(key: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CUSTOM_API_KEY] = key
        }
    }

    suspend fun clearAllData() {
        context.dataStore.edit { it.clear() }
    }
}
