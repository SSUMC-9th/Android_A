package com.example.umc_9th

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.umc_9th.data.Album
import com.example.umc_9th.data.AlbumDao
import com.example.umc_9th.data.Song
import com.example.umc_9th.data.SongDao

@Database(entities = [Song::class, Album::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao
    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // 버전 변경 시 데이터 삭제
                    .build()
            }
            return instance!!
        }
    }
}