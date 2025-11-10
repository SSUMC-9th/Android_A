package com.example.umc_9th

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.umc_9th.dao.SongDao
import com.example.umc_9th.db.SongDatabase
import com.example.umc_9th.entitiy.AlbumTableEntity
import com.example.umc_9th.entitiy.SongTableEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), HomeFragment.OnAlbumButtonClickListener {
    val uid = "asdfasdf"
    private lateinit var binding : ActivityMainBinding
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == RESULT_OK) {
            val returnString = result.data?.getStringExtra("title")
            val progress = result.data?.getIntExtra("seekBarPr", 0) ?: 0
            val max = result.data?.getIntExtra("seekBarMax", 100) ?: 100
            val id = result.data?.getIntExtra("songID", 100) ?: 100

            binding.seekBar.max = max
            binding.seekBar.progress = progress
            // TODO - fix to firebase
            val RoomDB = SongDatabase.getInstance(this)
            val SongDao = RoomDB.SongDao()
            val AlbumDao = RoomDB.AlbumDao()
            CoroutineScope(Dispatchers.IO).launch {
                val currentSongStorage = currentSongStorage(thisContext)
                currentSongStorage.setCurrentShow(id)
                withContext(Dispatchers.Main) {
                    binding.miniTitle.text = SongDao.getSongByIdx(currentSongStorage.getCurrentSong())[0].title
                    binding.miniSinger.text = SongDao.getSongByIdx(currentSongStorage.getCurrentSong())[0].singer
                }
            }
            val toast = Toast.makeText(this, returnString, Toast.LENGTH_SHORT) // in Activity
            toast.show()
        }
    }
    private var thisContext : Context = this
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없으면 요청
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, HomeFragment())
            .commit()
        // TODO - fix to firebase
        val RoomDB = SongDatabase.getInstance(this)
        val SongDao = RoomDB.SongDao()
        val AlbumDao = RoomDB.AlbumDao()
        // TODO - fix to firebase
        CoroutineScope(Dispatchers.IO).launch {
            val currentSongStorage = currentSongStorage(thisContext)
            Log.d("test", currentSongStorage.getCurrentSong().toString())
            Log.d("test", "size" + SongDao.getAllSong().size.toString())
            if(currentSongStorage.getCurrentSong()<=0) {
                currentSongStorage.setCurrentShow(1)
            }
            if(currentSongStorage.getCurrentSong() >= SongDao.getAllSong().size) {
                currentSongStorage.setCurrentShow(SongDao.getAllSong().size)
            }
            binding.miniTitle.text = SongDao.getSongByIdx(currentSongStorage.getCurrentSong())[0].title
            binding.miniSinger.text = SongDao.getSongByIdx(currentSongStorage.getCurrentSong())[0].singer

        }
        binding.back.setOnClickListener {
            // TODO - fix to firebase
            CoroutineScope(Dispatchers.IO).launch {
                val currentSongStorage = currentSongStorage(thisContext)
                if(currentSongStorage.getCurrentSong() > 1) {
                    currentSongStorage.setCurrentShow(currentSongStorage.getCurrentSong() - 1)
                    withContext(Dispatchers.Main) {
                        binding.miniTitle.text = SongDao.getSongByIdx(currentSongStorage.getCurrentSong())[0].title
                        binding.miniSinger.text = SongDao.getSongByIdx(currentSongStorage.getCurrentSong())[0].singer
                    }
                }
            }
        }
        binding.forward.setOnClickListener {
            // TODO - fix to firebase
            CoroutineScope(Dispatchers.IO).launch {
                val currentSongStorage = currentSongStorage(thisContext)
                if(currentSongStorage.getCurrentSong() < SongDao.getAllSong().size) {
                    currentSongStorage.setCurrentShow(currentSongStorage.getCurrentSong() + 1)
                    withContext(Dispatchers.Main) {
                        binding.miniTitle.text = SongDao.getSongByIdx(currentSongStorage.getCurrentSong())[0].title
                        binding.miniSinger.text = SongDao.getSongByIdx(currentSongStorage.getCurrentSong())[0].singer
                    }
                }
            }
        }

        binding.songButton.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", binding.miniTitle.text.toString())
            intent.putExtra("singer", binding.miniSinger.text.toString())
            intent.putExtra("seekBarPr", binding.seekBar.progress)
            intent.putExtra("seekBarMax", binding.seekBar.max)
            intent.putExtra("songID", currentSongStorage(thisContext).getCurrentSong())
            launcher.launch(intent)
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId){

                //매인 화면
                R.id.Home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, HomeFragment())
                        .commit()
                    true
                }

                //일기 작성 화면
                R.id.Locker -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, LockerFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
    override fun onAlbumButtonClicked(title : String, singer: String) {
        binding.miniTitle.text = title
        binding.miniSinger.text = singer
    }
}