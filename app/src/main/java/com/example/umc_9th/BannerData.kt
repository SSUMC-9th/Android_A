package com.example.umc_9th

data class BannerData(
    val title: String,              // 배너 제목 (예: "달빛의 감성 산책")
    val songs: List<BannerSong>     // 배너 안에 들어갈 곡들
)