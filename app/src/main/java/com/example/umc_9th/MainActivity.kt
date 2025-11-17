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
import androidx.lifecycle.lifecycleScope
import com.example.umc_9th.entitiy.SongTableEntity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), HomeFragment.OnAlbumButtonClickListener {

    private val uid = "asdfqwer1234"
    private val userSongsRef = FirebaseDatabase.getInstance().getReference("songs").child(uid)

    private lateinit var binding: ActivityMainBinding
    private val songList = mutableListOf<SongTableEntity>()
    private lateinit var thisContext: Context

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val returnString = result.data?.getStringExtra("title")
            val progress = result.data?.getIntExtra("seekBarPr", 0) ?: 0
            val max = result.data?.getIntExtra("seekBarMax", 100) ?: 100
            val id = result.data?.getIntExtra("songID", 1) ?: 1

            binding.seekBar.max = max
            binding.seekBar.progress = progress

            val song = songList.firstOrNull { it.id == id }
            if (song != null) {
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.miniTitle.text = song.title
                    binding.miniSinger.text = song.singer
                }

                lifecycleScope.launch(Dispatchers.IO) {
                    currentSongStorage(thisContext).setCurrentShow(id)
                }
            }

            Toast.makeText(this, returnString ?: "노래 정보 없음", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        thisContext = this

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, HomeFragment())
            .commit()
        loadSongs()

        binding.songButton.setOnClickListener {
            val currentId = currentSongStorage(thisContext).getCurrentSong()
            val currentSong = songList.firstOrNull { it.id == currentId }

            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", currentSong?.title ?: binding.miniTitle.text.toString())
                putExtra("singer", currentSong?.singer ?: binding.miniSinger.text.toString())
                putExtra("seekBarPr", binding.seekBar.progress)
                putExtra("seekBarMax", binding.seekBar.max)
                putExtra("songID", currentId)
            }
            launcher.launch(intent)
        }

        binding.back.setOnClickListener { moveToPreviousSong() }
        binding.forward.setOnClickListener { moveToNextSong() }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, HomeFragment())
                        .commit()
                    true
                }
                R.id.Search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, SearchFragment())
                        .commit()
                    true
                }
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

    private fun loadSongs() {
        userSongsRef.get()
            .addOnSuccessListener { dataSnapshot ->
                songList.clear()
                for (childSnapshot in dataSnapshot.children) {
                    val song = childSnapshot.getValue(SongTableEntity::class.java)
                    if (song != null) songList.add(song)
                }

                lifecycleScope.launch(Dispatchers.IO) {
                    val currentSongStorage = currentSongStorage(thisContext)
                    if (currentSongStorage.getCurrentSong() <= 0)
                        currentSongStorage.setCurrentShow(1)
                    if (currentSongStorage.getCurrentSong() > songList.size)
                        currentSongStorage.setCurrentShow(songList.size)

                    val current = songList.firstOrNull {
                        it.id == currentSongStorage.getCurrentSong()
                    }
                    current?.let { updateMiniPlayerUI(it) }
                }

                Log.d("Firebase", "노래 ${songList.size}개 로드 완료")
            }
            .addOnFailureListener {
                Log.e("Firebase", "데이터 로드 실패", it)
            }
    }

    private fun moveToPreviousSong() {
        lifecycleScope.launch(Dispatchers.IO) {
            val storage = currentSongStorage(thisContext)
            val currentId = storage.getCurrentSong()
            if (currentId > 1) {
                storage.setCurrentShow(currentId - 1)
                songList.firstOrNull { it.id == currentId - 1 }?.let {
                    updateMiniPlayerUI(it)
                }
            }
        }
    }

    private fun moveToNextSong() {
        lifecycleScope.launch(Dispatchers.IO) {
            val storage = currentSongStorage(thisContext)
            val currentId = storage.getCurrentSong()
            if (currentId < songList.size) {
                storage.setCurrentShow(currentId + 1)
                songList.firstOrNull { it.id == currentId + 1 }?.let {
                    updateMiniPlayerUI(it)
                }
            }
        }
    }

    private suspend fun updateMiniPlayerUI(song: SongTableEntity) {
        withContext(Dispatchers.Main) {
            binding.miniTitle.text = song.title
            binding.miniSinger.text = song.singer
        }
    }

    override fun onAlbumButtonClicked(title: String, singer: String) {
        binding.miniTitle.text = title
        binding.miniSinger.text = singer
    }
}
