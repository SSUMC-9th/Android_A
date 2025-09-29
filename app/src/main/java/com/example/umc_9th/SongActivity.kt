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
    private var isRepeatOn = false
    private var isShuffleOn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        val repeatBtn = findViewById<ImageButton>(R.id.btn_repeat)
        val shuffleBtn = findViewById<ImageButton>(R.id.btn_shuffle)

        // 반복 버튼 클릭 이벤트
        repeatBtn.setOnClickListener {
            isRepeatOn = !isRepeatOn
            if (isRepeatOn) {
                repeatBtn.setImageResource(R.drawable.nugu_btn_down)
            } else {
                repeatBtn.setImageResource(R.drawable.nugu_btn_repeat_inactive)
            }
        }

        // 전체재생(셔플) 버튼 클릭 이벤트
        shuffleBtn.setOnClickListener {
            isShuffleOn = !isShuffleOn
            if (isShuffleOn) {
                shuffleBtn.setImageResource(R.drawable.nugu_btn_down)
            } else {
                shuffleBtn.setImageResource(R.drawable.nugu_btn_random_inactive)
            }
        }

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

        findViewById<ImageView>(R.id.song_album_image).setOnClickListener {
            val bottomSheet = AlbumBottomSheetFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }


    }
}

