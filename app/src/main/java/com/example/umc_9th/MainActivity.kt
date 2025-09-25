package com.example.umc_9th

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityMainBinding

class ExploreFragment : Fragment(R.layout.fragment_explore)
class SearchFragment : Fragment(R.layout.fragment_search)
class LibraryFragment : Fragment(R.layout.fragment_locker)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val songActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val title = result.data?.getStringExtra("title")
            val artist = result.data?.getStringExtra("artist")
            val albumResId = result.data?.getIntExtra("albumResId", R.drawable.btn_textbox_close)

            binding.miniPlayerTitle.text = title
            binding.miniPlayerArtist.text = artist
            albumResId?.let {
                binding.miniPlayerAlbum.setImageResource(it)
                binding.miniPlayerAlbum.tag = it
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commit()
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.bottom_homeButton -> HomeFragment()
                R.id.bottom_lookButton -> ExploreFragment()
                R.id.bottom_searchButton -> SearchFragment()
                R.id.bottom_lockerButton -> LibraryFragment()
                else -> HomeFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, selectedFragment)
                .commit()

            true
        }

        binding.gotoSong.setOnClickListener {
            val title= binding.miniPlayerTitle.text.toString()
            val artist = binding.miniPlayerArtist.text.toString()
            val albumResId = binding.miniPlayerAlbum.tag as? Int

            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", title)
                putExtra("artist", artist)
                putExtra("albumResId", albumResId)
            }
            songActivityLauncher.launch(intent)
        }

    }
}
