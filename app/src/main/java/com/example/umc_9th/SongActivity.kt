package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_8th.R

class SongActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        val title = intent.getStringExtra("title")
        val artist = intent.getStringExtra("artist")
        val albumResId = intent.getIntExtra("albumResId", R.drawable.btn_textbox_close)
        findViewById<TextView>(R.id.song_title).text = title
        findViewById<TextView>(R.id.song_artist).text = artist
        findViewById<ImageView>(R.id.song_album_image).setImageResource(albumResId)

        findViewById<ImageButton>(R.id.song_drop_button).setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("title", title)
            resultIntent.putExtra("artist", artist)
            resultIntent.putExtra("albumResId", albumResId)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }
}

