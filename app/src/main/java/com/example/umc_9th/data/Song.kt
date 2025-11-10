package com.example.umc_9th.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//Song table Entity
@Entity(tableName = "Song_table")
data class Song (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var title: String = "",
    var singer: String = "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: Int = 0,
    var coverImg: Int? = null,
    var isLike: Boolean = false,
    var albumIdx: Int = 0
)
