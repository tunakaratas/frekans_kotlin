package com.foursoftware.frekans.data
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "frekans_settings",
        Context.MODE_PRIVATE
    )
    private val _defaultVolume = MutableStateFlow<Float>(
        prefs.getFloat("default_volume", 0.5f)
    )
    val defaultVolume: StateFlow<Float> = _defaultVolume.asStateFlow()
    fun setDefaultVolume(volume: Float) {
        val clampedVolume = volume.coerceIn(0f, 1f)
        _defaultVolume.value = clampedVolume
        prefs.edit().putFloat("default_volume", clampedVolume).apply()
    }
    private val _notificationsEnabled = MutableStateFlow<Boolean>(
        prefs.getBoolean("notifications_enabled", true)
    )
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()
    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }
    private val _darkModeEnabled = MutableStateFlow<Boolean>(
        prefs.getBoolean("dark_mode_enabled", false)
    )
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled.asStateFlow()
    fun setDarkModeEnabled(enabled: Boolean) {
        _darkModeEnabled.value = enabled
        prefs.edit().putBoolean("dark_mode_enabled", enabled).apply()
    }
    private val _themeColor = MutableStateFlow<Int>(
        prefs.getInt("theme_color", 0)
    )
    val themeColor: StateFlow<Int> = _themeColor.asStateFlow()
    fun setThemeColor(color: Int) {
        val clampedColor = color.coerceIn(0, 2)
        _themeColor.value = clampedColor
        prefs.edit().putInt("theme_color", clampedColor).apply()
    }
}
