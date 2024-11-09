package com.nat.submissionnavigationapi.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.nat.submissionnavigationapi.database.EventDao
import com.nat.submissionnavigationapi.database.EventDatabase
import com.nat.submissionnavigationapi.database.FavoriteEvent

class EventRepository(private val favoriteEventDao: EventDao) {

    // Mengambil semua event favorit
    val favoriteEvents: LiveData<List<FavoriteEvent>> = favoriteEventDao.getFavoriteEvents()

    // Menambahkan event ke favorit
    suspend fun insertFavoriteEvent(event: FavoriteEvent) {
        try {
            favoriteEventDao.insert(event)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error inserting favorite event: ${e.message}")
            throw e // Lemparkan kembali exception jika diperlukan
        }
    }

    // Menghapus event dari favorit
    suspend fun deleteFavoriteEvent(event: FavoriteEvent) {
        try {
            favoriteEventDao.delete(event)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error deleting favorite event: ${e.message}")
            throw e // Lemparkan kembali exception jika diperlukan
        }
    }

    // Mengambil event favorit berdasarkan ID
    fun getFavoriteEventById(eventId: Int): LiveData<FavoriteEvent?> {
        return favoriteEventDao.getFavoriteEventById(eventId)
    }

    // Mengecek apakah event adalah favorit
    suspend fun isFavorite(eventId: Int): Boolean { // Ubah menjadi suspend dan kembalikan Boolean
        return favoriteEventDao.isFavorite(eventId)
    }

    // Objek untuk penyedia repository
    object Injection {
        fun provideRepository(application: Application): EventRepository {
            val database = EventDatabase.getDatabase(application)
            val eventDao = database.eventDao()
            return EventRepository(eventDao)
        }
    }
}