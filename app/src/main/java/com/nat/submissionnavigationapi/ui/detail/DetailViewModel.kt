package com.nat.submissionnavigationapi.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.submissionnavigationapi.database.FavoriteEvent
import com.nat.submissionnavigationapi.repository.EventRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {
    fun getFavoriteEventById(id: Int): LiveData<FavoriteEvent?> =
        repository.getFavoriteEventById(id)

    fun insertFavoriteEvent(favoriteEvent: FavoriteEvent) = viewModelScope.launch {
        repository.insertFavoriteEvent(favoriteEvent)
    }

    fun deleteFavoriteEvent(favoriteEvent: FavoriteEvent) = viewModelScope.launch {
        repository.deleteFavoriteEvent(favoriteEvent)
    }
}
