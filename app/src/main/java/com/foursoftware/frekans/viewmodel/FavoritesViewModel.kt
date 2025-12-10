package com.foursoftware.frekans.viewmodel
import android.content.Context
import androidx.lifecycle.ViewModel
import com.foursoftware.frekans.data.FavoritesRepository
import kotlinx.coroutines.flow.StateFlow
class FavoritesViewModel(context: Context) : ViewModel() {
    private val repository = FavoritesRepository(context)
    val favorites: StateFlow<Set<Int>> = repository.favorites
    fun toggleFavorite(plantId: Int) {
        repository.toggleFavorite(plantId)
    }
    fun isFavorite(plantId: Int): Boolean {
        return repository.isFavorite(plantId)
    }
    fun getFavoritePlants() = repository.getFavoritePlants()
}
