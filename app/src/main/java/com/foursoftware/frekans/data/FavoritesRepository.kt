package com.foursoftware.frekans.data
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class FavoritesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "frekans_favorites",
        Context.MODE_PRIVATE
    )
    private val _favorites = MutableStateFlow<Set<Int>>(loadFavorites())
    val favorites: StateFlow<Set<Int>> = _favorites.asStateFlow()
    private fun loadFavorites(): Set<Int> {
        val favoritesString = prefs.getString("favorites", "") ?: ""
        return if (favoritesString.isEmpty()) {
            emptySet()
        } else {
            favoritesString.split(",").mapNotNull { it.toIntOrNull() }.toSet()
        }
    }
    private fun saveFavorites(favorites: Set<Int>) {
        prefs.edit()
            .putString("favorites", favorites.joinToString(","))
            .apply()
    }
    fun toggleFavorite(plantId: Int) {
        val currentFavorites = _favorites.value.toMutableSet()
        if (currentFavorites.contains(plantId)) {
            currentFavorites.remove(plantId)
        } else {
            currentFavorites.add(plantId)
        }
        _favorites.value = currentFavorites
        saveFavorites(currentFavorites)
    }
    fun isFavorite(plantId: Int): Boolean {
        return _favorites.value.contains(plantId)
    }
    fun getFavoritePlants(): List<Plant> {
        return PlantRepository.plants.filter { _favorites.value.contains(it.id) }
    }
}
