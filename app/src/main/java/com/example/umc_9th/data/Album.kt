package com.example.umc_9th.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

//Album table Entity
@Entity(tableName = "album_table")
data class Album (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var title: String = "",
    var singer: String = "",
    var isLike: Boolean = false,
    var coverImg: Int? = null,

    @Ignore
    var songs: List<Song> = emptyList()

)