package com.example.umc_9th

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.umc_9th.data.Album
import com.example.umc_9th.data.AlbumDao
import com.example.umc_9th.data.Song
import com.example.umc_9th.data.SongDao

@Database(entities = [Song::class, Album::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao

    companion object{
        // "아직 데이터베이스가 한 번도 생성되지 않은 상태"를 담기 위해 null을 허용
        @Volatile
        private var INSTANCE: com.example.umc_9th.AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "song-database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}