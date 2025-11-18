package com.example.umc_9th.data.firebase

import com.example.umc_9th.Album
import com.google.firebase.database.*
import com.example.umc_9th.Song

class FirebaseManager {
    
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val songsRef = database.child("songs")
    private val likedSongsRef = database.child("likedSongs")
    private val likedAlbumsRef = database.child("likedAlbums")
    
    companion object {
        @Volatile
        private var instance: FirebaseManager? = null
        
        fun getInstance(): FirebaseManager {
            return instance ?: synchronized(this) {
                instance ?: FirebaseManager().also { instance = it }
            }
        }
    }
    
    // ===== 좋아요 관련 함수 =====
    
    /**
     * 좋아요 추가
     */
    fun addLikedSong(song: Song, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
        val songData = hashMapOf(
            "id" to song.id,
            "title" to song.title,
            "singer" to song.singer,
            "second" to song.second,
            "playTime" to song.playTime,
            "isPlaying" to song.isPlaying,
            "music" to song.music,
            "coverImg" to song.coverImg,
            "isLike" to true,
            "albumIdx" to song.albumIdx
        )
        
        likedSongsRef.child(song.id.toString()).setValue(songData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Failed to add liked song") }
    }
    
    /**
     * 좋아요 제거
     */
    fun removeLikedSong(songId: Int, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
        likedSongsRef.child(songId.toString()).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Failed to remove liked song") }
    }
    
    /**
     * 좋아요 상태 업데이트
     */
    fun updateLikeStatus(songId: Int, isLike: Boolean, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
        if (isLike) {
            likedSongsRef.child(songId.toString()).child("isLike").setValue(true)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e.message ?: "Failed to update like") }
        } else {
            removeLikedSong(songId, onSuccess, onFailure)
        }
    }
    
    /**
     * 좋아요한 노래 목록 가져오기
     */
    fun getLikedSongs(onSuccess: (List<Song>) -> Unit, onFailure: (String) -> Unit = {}) {
        likedSongsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likedSongs = mutableListOf<Song>()
                
                for (childSnapshot in snapshot.children) {
                    try {
                        val id = childSnapshot.child("id").getValue(Int::class.java) ?: 0
                        val title = childSnapshot.child("title").getValue(String::class.java) ?: ""
                        val singer = childSnapshot.child("singer").getValue(String::class.java) ?: ""
                        val second = childSnapshot.child("second").getValue(Int::class.java) ?: 0
                        val playTime = childSnapshot.child("playTime").getValue(Int::class.java) ?: 0
                        val isPlaying = childSnapshot.child("isPlaying").getValue(Boolean::class.java) ?: false
                        val music = childSnapshot.child("music").getValue(String::class.java) ?: ""
                        val coverImg = childSnapshot.child("coverImg").getValue(Int::class.java)
                        val isLike = childSnapshot.child("isLike").getValue(Boolean::class.java) ?: true
                        val albumIdx = childSnapshot.child("albumIdx").getValue(Int::class.java) ?: 0
                        
                        val song = Song(
                            id = id,
                            title = title,
                            singer = singer,
                            second = second,
                            playTime = playTime,
                            isPlaying = isPlaying,
                            music = music,
                            coverImg = coverImg,
                            isLike = isLike,
                            albumIdx = albumIdx
                        )
                        likedSongs.add(song)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                
                onSuccess(likedSongs)
            }
            
            override fun onCancelled(error: DatabaseError) {
                onFailure(error.message)
            }
        })
    }
    
    /**
     * 특정 노래가 좋아요 되어 있는지 확인
     */
    fun checkIfLiked(songId: Int, onResult: (Boolean) -> Unit) {
        likedSongsRef.child(songId.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onResult(snapshot.exists())
            }
            
            override fun onCancelled(error: DatabaseError) {
                onResult(false)
            }
        })
    }

    // 🔥 ========== 앨범 관련 함수들 추가 ==========

    // 앨범 좋아요 추가
    fun addLikedAlbum(album: Album, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val albumData = mapOf(
            "id" to album.id,
            "title" to album.title,
            "artist" to album.artist,
            "albumResId" to album.albumResId
        )

        database.child("likedAlbums").child(album.id.toString()).setValue(albumData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "오류") }
    }

    // 앨범 좋아요 제거
    fun removeLikedAlbum(albumId: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        database.child("likedAlbums").child(albumId.toString()).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "오류") }
    }

    // 앨범 좋아요 상태 확인
    fun checkIfAlbumLiked(albumId: Int, callback: (Boolean) -> Unit) {
        database.child("likedAlbums").child(albumId.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot.exists())
                }
                override fun onCancelled(error: DatabaseError) {
                    callback(false)
                }
            })
    }

    // 좋아요한 앨범 목록
    fun getLikedAlbums(onSuccess: (List<Album>) -> Unit, onFailure: (String) -> Unit) {
        database.child("likedAlbums").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val albums = mutableListOf<Album>()
                for (child in snapshot.children) {
                    val id = child.child("id").getValue(Int::class.java) ?: 0
                    val title = child.child("title").getValue(String::class.java) ?: ""
                    val artist = child.child("artist").getValue(String::class.java) ?: ""
                    val albumResId = child.child("albumResId").getValue(Int::class.java) ?: 0

                    albums.add(Album(id, title, artist, albumResId, true))
                }
                onSuccess(albums)
            }
            override fun onCancelled(error: DatabaseError) {
                onFailure(error.message)
            }
        })
    }

    
    // ===== 노래 재생 위치 저장 =====
    
    /**
     * 현재 재생 위치 저장
     */
    fun savePlaybackPosition(songId: Int, position: Int) {
        songsRef.child(songId.toString()).child("second").setValue(position)
    }
    
    /**
     * 재생 상태 저장
     */
    fun savePlaybackStatus(songId: Int, isPlaying: Boolean) {
        songsRef.child(songId.toString()).child("isPlaying").setValue(isPlaying)
    }
    
    /**
     * 현재 재생 중인 노래 ID 저장
     */
    fun saveCurrentSongId(songId: Int) {
        database.child("currentSong").setValue(songId)
    }
    
    /**
     * 현재 재생 중인 노래 ID 가져오기
     */
    fun getCurrentSongId(onResult: (Int) -> Unit) {
        database.child("currentSong").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val songId = snapshot.getValue(Int::class.java) ?: 1
                onResult(songId)
            }
            
            override fun onCancelled(error: DatabaseError) {
                onResult(1)
            }
        })
    }
}
