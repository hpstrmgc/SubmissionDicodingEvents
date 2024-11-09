package com.nat.submissionnavigationapi.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: FavoriteEvent)

    @Delete
    suspend fun delete(event: FavoriteEvent)

    @Query("SELECT * FROM favorite_events")
    fun getFavoriteEvents(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_events WHERE id = :id LIMIT 1")
    fun getFavoriteEventById(id: Int): LiveData<FavoriteEvent?>

    @Query("SELECT EXISTS(SELECT * FROM favorite_events WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean // Changed to suspend function and Boolean return type
}