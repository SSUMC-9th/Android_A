package com.example.umc_9th

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.umc_9th.Song
import com.example.umc_9th.SongActivity
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    // SongActivity 결과를 받는 콜백 등록
    private val songActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val albumTitle = result.data?.getStringExtra("albumTitle")
                albumTitle?.let {
                    Toast.makeText(this, "받은 앨범 제목: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 화면으로 HomeFragment 설정
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_frm) as NavHostFragment
        val navController = navHostFragment.navController

        binding.mainBnv.setupWithNavController(navController)

    }
    // ✅ 미니플레이어 업데이트 함수
    fun updateMiniPlayer(album: Album) {
        binding.mainMiniplayerTitleTv.text = album.title
        binding.mainMiniplayerSingerTv.text = album.singer
    }

}