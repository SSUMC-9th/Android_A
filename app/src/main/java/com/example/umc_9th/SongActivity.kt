package com.example.umc_9th

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongBinding
    lateinit var title : String
    lateinit var singer : String
    var repeat : Boolean = false
    var suffle : Boolean = false
    var playing : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = intent.getStringExtra("title").toString()
        singer = intent.getStringExtra("singer").toString()
        binding.titleText.text = title
        binding.singerText.text = singer
        binding.repeatButton.setColorFilter(Color.rgb(140, 140, 140))
        binding.suffleButton.setColorFilter(Color.rgb(140, 140, 140))
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.repeatButton.setOnClickListener {
            if(!repeat) binding.repeatButton.setColorFilter(Color.rgb(73, 6, 204))
            else binding.repeatButton.setColorFilter(Color.rgb(140, 140, 140))
            repeat = !repeat
        }
        binding.suffleButton.setOnClickListener {
            if(!suffle) binding.suffleButton.setColorFilter(Color.rgb(73,6,204))
            else binding.suffleButton.setColorFilter(Color.rgb(140,140,140))
            suffle = !suffle
        }
        binding.playButton.setOnClickListener {
            if(playing) binding.playButton.setImageResource(R.drawable.btn_miniplay_mvplay)
            else binding.playButton.setImageResource(R.drawable.btn_miniplay_mvpause)
            playing = !playing
        }
    }

    override fun onDestroy() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivity.STRING_INTENT_KEY, title)
        }
        setResult(RESULT_OK, intent)
        super.onDestroy()
    }
}