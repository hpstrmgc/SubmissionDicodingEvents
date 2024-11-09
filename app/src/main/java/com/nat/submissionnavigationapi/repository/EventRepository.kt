package com.nat.submissionnavigationapi.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.nat.submissionnavigationapi.database.EventDao
import com.nat.submissionnavigationapi.database.EventDatabase
import com.nat.submissionnavigationapi.database.FavoriteEvent

class EventRepository(private val favoriteEventDao: EventDao) {

    val favoriteEvents: LiveData<List<FavoriteEvent>> = favoriteEventDao.getFavoriteEvents()

    suspend fun insertFavoriteEvent(event: FavoriteEvent) {
        try {
            favoriteEventDao.insert(event)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error inserting favorite event: ${e.message}")
            throw e
        }
    }

    suspend fun deleteFavoriteEvent(event: FavoriteEvent) {
        try {
            favoriteEventDao.delete(event)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error deleting favorite event: ${e.message}")
            throw e
        }
    }

    fun getFavoriteEventById(eventId: Int): LiveData<FavoriteEvent?> {
        return favoriteEventDao.getFavoriteEventById(eventId)
    }

    suspend fun isFavorite(eventId: Int): Boolean {
        return favoriteEventDao.isFavorite(eventId)
    }

    object Injection {
        fun provideRepository(application: Application): EventRepository {
            val database = EventDatabase.getDatabase(application)
            val eventDao = database.eventDao()
            return EventRepository(eventDao)
        }
    }
}