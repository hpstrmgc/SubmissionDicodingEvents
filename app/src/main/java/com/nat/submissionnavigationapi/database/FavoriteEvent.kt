package com.nat.submissionnavigationapi.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey val id: Int,
    val name: String,
    val beginTime: String,
    val mediaCover: String
)
