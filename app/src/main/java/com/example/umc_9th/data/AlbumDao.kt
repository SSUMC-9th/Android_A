package com.example.umc_9th.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Query("SELECT * FROM Album_table")
    fun getAlbums(): List<Album>
}