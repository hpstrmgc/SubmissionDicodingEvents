package com.nat.submissionnavigationapi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoriteEvent::class], version = 2, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. Buat tabel sementara dengan skema baru
                database.execSQL(
                    "CREATE TABLE favorite_events_new (id INTEGER PRIMARY KEY NOT NULL, name TEXT, beginTime TEXT, mediaCover TEXT)"
                )

                // 2. Salin data dari tabel lama ke tabel baru
                database.execSQL(
                    "INSERT INTO favorite_events_new (id, name, beginTime, mediaCover) SELECT id, name, beginTime, mediaCover FROM favorite_events"
                )

                // 3. Hapus tabel lama
                database.execSQL("DROP TABLE favorite_events")

                // 4. Ubah nama tabel baru menjadi nama tabel lama
                database.execSQL("ALTER TABLE favorite_events_new RENAME TO favorite_events")
            }
        }

        fun getDatabase(context: Context): EventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "event_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}