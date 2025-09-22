package com.redcaramel.umc_misson_2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.redcaramel.umc_misson_2.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongBinding
    lateinit var title : String
    lateinit var singer : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = intent.getStringExtra("title").toString()
        singer = intent.getStringExtra("singer").toString()
        binding.backButton.setOnClickListener {
            finish()
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