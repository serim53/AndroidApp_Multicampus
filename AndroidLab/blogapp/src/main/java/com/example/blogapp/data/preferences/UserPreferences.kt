package com.example.blogapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_preferences")


@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val KEY_EMAIL = stringPreferencesKey("user_email")
    }

    val email: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_EMAIL] ?: ""
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_EMAIL] = email
        }
    }
}