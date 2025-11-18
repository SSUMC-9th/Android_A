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

        //Handler를 이용해 2초 후에 MainActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            //이동한 다음 다시 스플래시 화면으로 이동하는 것 방지
            finish()

        }, 2000) //2초 후 실행
    }
}