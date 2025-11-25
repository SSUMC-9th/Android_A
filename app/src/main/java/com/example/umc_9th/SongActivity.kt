
package com.example.umc_9th

import umc.study.umc_8th.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umc_9th.data.Song
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import umc.study.umc_8th.databinding.ActivitySongBinding
import java.text.SimpleDateFormat
import java.util.*

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    private var song: Song? = null

    private lateinit var songDB: AppDatabase
    private var songs = ArrayList<Song>() // DB에서 가져올 '전체 곡 리스트'
    private var nowPos = 0 // '전체 곡 리스트' 중 현재 곡의 인덱스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songDB = AppDatabase.getInstance(this)

        initSongAndPlayer() // UI 초기화
        loadSongsFromDB() // DB에서 전체 곡 리스트 로드
        observePlayerState() // 관리자 상태 관찰 시작

        // 모든 버튼이 MusicPlayerManager를 제어하도록
        binding.songMiniplayerIv.setOnClickListener {
            MusicPlayerManager.play()
        }
        binding.songPauseIv.setOnClickListener {
            MusicPlayerManager.pause()
        }
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }
        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songLikeIv.setOnClickListener {
            toggleLikeStatus()
        }

        // SeekBar 리스너
        binding.songProgressbarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.songStartTimeTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let {
                    MusicPlayerManager.seekTo(it) // 사용자가 터치를 떼면 관리자에게 알림
                }
            }
        })
    }

    private fun moveSong(direction: Int) {
        if (songs.isEmpty()) return

        // 1. nowPos 값을 변경 (이전/다음)
        nowPos += direction

        // 2. nowPos가 리스트 범위를 벗어났는지 확인
        if (nowPos < 0){ // 첫 곡에서 '이전'을 누르면
            nowPos = songs.size - 1 //n 마지막 곡으로
        } else if (nowPos >= songs.size) { // 마지막 곡에서 '다음'을 누르면
            nowPos = 0 // 첫 곡으로
        }

        // 3. 새 위치에 맞는 곡을 Manager에 로드하고 재생
        val newSong = songs[nowPos]
        MusicPlayerManager.loadSong(this, newSong)
        MusicPlayerManager.play()

        setPlayerUI(newSong)
    }

    // 하트 버튼을 눌렀을 때 호출되는 메인 함수
    private fun toggleLikeStatus(){
        // 1. Manager로부터 '현재 곡'을 직접 가져옵니다.
        val currentSong = MusicPlayerManager.currentSong ?: return

        // 2. '현재 곡'의 '좋아요' 상태를 반대로 뒤집습니다.
        currentSong.isLike = !currentSong.isLike

        // 3. 하트 아이콘 UI를 즉시 업데이트합니다.
        updateLikeButtonUI(currentSong.isLike)

        // 4. 변경된 '현재 곡' 객체를 DB에 업데이트합니다.
        updateSongInDB(currentSong)

        // 5. (선택적) DB에서 불러온 'songs' 리스트와도 상태를 동기화합니다.
        //    (DB 로드가 끝났다는 보장이 없으므로 안전하게 확인)
        if (songs.isNotEmpty() && nowPos < songs.size && songs[nowPos].id == currentSong.id) {
            songs[nowPos].isLike = currentSong.isLike
        }
    }

    // 하트 아이콘의 이미지를 바꾸는 함수
    private fun updateLikeButtonUI(isLiked: Boolean) {
        if (isLiked){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    // RoomDB에 'Song' 객체를 업데이트
    private fun updateSongInDB(song: Song){
        lifecycleScope.launch(Dispatchers.IO) {
            songDB.songDao().update(song)
        }
    }

    private fun loadSongsFromDB() {
        lifecycleScope.launch(Dispatchers.IO) {
            // 1. DB에서 모든 노래를 가져옴
            val songListFromDB = songDB.songDao().getAllSongs()

            // 2. SongActivity의 'songs' 리스트를 채움
            songs.clear()
            songs.addAll(songListFromDB)

            // 3. DB 로드가 완료된 후, 현재 곡의 위치를 찾음
            findCurrentSongPosition()

            // 4. UI 표시는 메인 스레드에서 진행
            withContext(Dispatchers.Main) {
                MusicPlayerManager.updatePlaylist(songs)
            }
        }
    }

    private fun findCurrentSongPosition() {
        val currentSongId = MusicPlayerManager.currentSong?.id

        if (currentSongId != null) {
            // 2. DB에서 가져온 'songs' 리스트를 반복하여 현재 곡 ID와 일치하는 곡의 인덱스를 찾
            this.nowPos = songs.indexOfFirst { it.id == currentSongId }

            // (안전장치) 만약 리스트에 현재 곡이 없으면 0으로 설정
            if (this.nowPos == -1) {
                this.nowPos = 0
            }
        } else {
            this.nowPos = 0
        }

        Log.d("SongActivity", "현재 곡 위치(nowPos): $nowPos")
    }

    private fun initSongAndPlayer() {
        // 관리자로부터 현재 곡 정보를 가져옴
        this.song = MusicPlayerManager.currentSong

        if (song == null) {
            finish() // 재생 중인 곡이 없으면 액티비티 종료
            return
        }
        setPlayerUI(song!!)
    }

    private fun setPlayerUI(currentSong: Song) {
        binding.songMusicTitleTv.text = currentSong.title
        binding.songSingerNameTv.text = currentSong.singer

        val duration = currentSong.playTime * 1000
        binding.songEndTimeTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
        binding.songProgressbarSb.max = duration

        // 노래가 로드될 때, 저장된 '좋아요' 상태를 UI에 반영
        updateLikeButtonUI(currentSong.isLike)
    }

    private fun observePlayerState() {
        lifecycleScope.launch {
            // 1. 재생 상태(재생/멈춤)가 바뀌면 버튼 아이콘 업데이트
            MusicPlayerManager.isPlaying.collect { isPlaying ->
                if (isPlaying) {
                    binding.songMiniplayerIv.visibility = View.GONE
                    binding.songPauseIv.visibility = View.VISIBLE
                } else {
                    binding.songMiniplayerIv.visibility = View.VISIBLE
                    binding.songPauseIv.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            // 2. 재생 시간이 바뀌면 SeekBar와 시간 텍스트 업데이트
            MusicPlayerManager.playbackPosition.collect { position ->
                binding.songProgressbarSb.progress = position
                binding.songStartTimeTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(position)

                // 현재 재생 시간을 song 객체에 저장
                song?.second = position / 1000
            }
        }

        lifecycleScope.launch {
            // 3. 실제 음악 길이를 구독
            MusicPlayerManager.duration.collect { duration ->
                if (duration > 0) {
                    binding.songEndTimeTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
                    binding.songProgressbarSb.max = duration
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        MusicPlayerManager.updateCurrentSecond()

        MusicPlayerManager.currentSong?.let { song ->
            lifecycleScope.launch(Dispatchers.IO){
                val db = AppDatabase.getInstance(applicationContext)
                db.songDao().update(song)
            }
        }
    }
}
