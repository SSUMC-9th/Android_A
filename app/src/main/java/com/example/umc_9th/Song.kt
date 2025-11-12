package com.example.umc_9th

data class Song(
    val id: Int,
    val title: String,
    val singer: String,
    var second: Int,
    val playTime: Int,
    var isPlaying: Boolean,
    val music: String,
    val coverImg: Int?,
    var isLike: Boolean,
    val albumIdx: Int
)