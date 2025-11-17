package com.example.umc_9th.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "AlbumTable")
data class AlbumTableEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var title: String = "",
    var singer: String = "",
    var isLike: Boolean = false,
    var coverImg: Int? = null
)