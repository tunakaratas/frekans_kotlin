package com.foursoftware.frekans.viewmodel
import android.content.Context
import androidx.lifecycle.ViewModel
import com.foursoftware.frekans.data.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
class SettingsViewModel(context: Context) : ViewModel() {
    private val repository = SettingsRepository(context)
    val defaultVolume: StateFlow<Float> = repository.defaultVolume
    val notificationsEnabled: StateFlow<Boolean> = repository.notificationsEnabled
    val darkModeEnabled: StateFlow<Boolean> = repository.darkModeEnabled
    val themeColor: StateFlow<Int> = repository.themeColor
    fun setDefaultVolume(volume: Float) {
        repository.setDefaultVolume(volume)
    }
    fun setNotificationsEnabled(enabled: Boolean) {
        repository.setNotificationsEnabled(enabled)
    }
    fun setDarkModeEnabled(enabled: Boolean) {
        repository.setDarkModeEnabled(enabled)
    }
    fun setThemeColor(color: Int) {
        repository.setThemeColor(color)
    }
}
class SettingsViewModelFactory(private val context: Context) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
