package com.example.umc_9th.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umc_9th.AppDatabase
import com.example.umc_9th.data.Album
import com.example.umc_9th.data.Song
import com.example.umc_9th.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import umc.study.umc_8th.R

class SplashActivity : AppCompatActivity() {
    private lateinit var songDB: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        songDB = AppDatabase.getInstance(this)
        checkAndInsertDummyData()

        //Handler를 이용해 2초 후에 MainActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            //이동한 다음 다시 스플래시 화면으로 이동하는 것 방지
            finish()

        }, 2000) //2초 후 실행
    }

    private fun checkAndInsertDummyData() {
        // 백그라운드 스레드에서 DB 작업
        lifecycleScope.launch(Dispatchers.IO) {
            val albums = songDB.albumDao().getAlbums()

            // ❗ DB가 비어있을 때 (앱 최초 실행 시) 1회만 실행
            if (albums.isEmpty()) {

                songDB.albumDao().insert(Album(id = 1, title = "LILAC", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2))
                songDB.albumDao().insert(Album(id = 2, title = "Butter", singer = "방탄소년단 (BTS)", coverImg = R.drawable.img_album_exp))
                songDB.albumDao().insert(Album(id = 3, title = "Next Level", singer = "aespa", coverImg = R.drawable.img_album_exp3))

                // --- 2. 수록곡 추가 ---
                // (albumIdx를 위 앨범 ID에 맞춰서 설정)
                songDB.songDao().insert(Song(title = "LILAC", singer = "아이유 (IU)", playTime = 215, music = R.raw.song_sample, coverImg = R.drawable.img_album_exp2, albumIdx = 1, isLike = true))
                songDB.songDao().insert(Song(title = "flu", singer = "아이유 (IU)", playTime = 200, music = R.raw.song_sample, coverImg = R.drawable.img_album_exp2, albumIdx = 1))
                songDB.songDao().insert(Song(title = "Coin", singer = "아이유 (IU)", playTime = 210, music = R.raw.song_sample, coverImg = R.drawable.img_album_exp2, albumIdx = 1))
                songDB.songDao().insert(Song(title = "봄 안녕 봄", singer = "아이유 (IU)", playTime = 210, music = R.raw.song_sample, coverImg = R.drawable.img_album_exp2, albumIdx = 1))
                songDB.songDao().insert(Song(title = "Celebrity", singer = "아이유 (IU)", playTime = 210, music = R.raw.song_sample, coverImg = R.drawable.img_album_exp2, albumIdx = 1))
                songDB.songDao().insert(Song(title = "돌림노래", singer = "아이유 (IU)", playTime = 210, music = R.raw.song_sample, coverImg = R.drawable.img_album_exp2, albumIdx = 1))

                songDB.songDao().insert(Song(title = "Butter", singer = "방탄소년단 (BTS)", playTime = 180, music = R.raw.song_sample, coverImg = R.drawable.img_album_exp, albumIdx = 2))
                songDB.songDao().insert(Song(title = "Next Level", singer = "aespa", playTime = 220, music = R.raw.song_sample, coverImg = R.drawable.img_album_exp3, albumIdx = 3))
            }
        }
    }
}