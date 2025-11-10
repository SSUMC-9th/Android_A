package com.example.umc_9th.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.umc_9th.dao.AlbumDao
import com.example.umc_9th.dao.SongDao
import com.example.umc_9th.entitiy.AlbumTableEntity
import com.example.umc_9th.entitiy.SongTableEntity

@Database(entities = [SongTableEntity::class, AlbumTableEntity::class], version = 1, exportSchema = false)
abstract class SongDatabase : RoomDatabase() {

    abstract fun SongDao(): SongDao
    abstract fun AlbumDao(): AlbumDao

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getInstance(context: Context): SongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "song_database" // DB 파일 이름
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}