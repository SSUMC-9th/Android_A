package com.example.umc_9th.data

import androidx.room.*

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    // song_table의 모든 데이터를 조회
    @Query("SELECT * FROM song_table")
    fun getAllSongs(): List<Song>

    // id를 기준으로 특정 곡 하나만 조회
    @Query("SELECT * FROM song_table WHERE id = :id")
    fun getSong(id: Int): Song?

    @Query("SELECT * FROM song_table WHERE isLike = 1")
    fun getLikedSongs(): List<Song>

    @Query("SELECT * FROM song_table WHERE albumIdx = :albumId")
    fun getSongsInAlbum(albumId: Int): List<Song>
}