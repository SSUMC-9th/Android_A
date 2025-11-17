package com.example.umc_9th.entitiy

data class SongTableEntity (
    var id: Int = 0,

    var title: String = "",
    var singer: String = "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var coverImg: Int? = null,
    var isLike: Boolean = false,
    var albumIdx: Int = 0
)