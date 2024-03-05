package com.example.utttilife.components.SharedPreferencesManager

import android.content.Context


object SharedPreferencesManager {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USERNAME = "username"

        fun saveLoggedInUser(context: Context, username: String) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(KEY_USERNAME, username).apply()
        }

        fun getLoggedInUser(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_USERNAME, null)
        }

        fun clearLoggedInUser(context: Context) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().remove(KEY_USERNAME).apply()
        }
    }
