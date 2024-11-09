package com.nat.submissionnavigationapi.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.submissionnavigationapi.database.FavoriteEvent
import com.nat.submissionnavigationapi.repository.EventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {
    val favoriteEvents: LiveData<List<FavoriteEvent>> = repository.favoriteEvents

    fun addOrRemoveFavorite(event: FavoriteEvent) {
        viewModelScope.launch {
            if (repository.isFavorite(event.id)) {
                repository.deleteFavoriteEvent(event)
            } else {
                repository.insertFavoriteEvent(event)
            }
        }
    }
}
