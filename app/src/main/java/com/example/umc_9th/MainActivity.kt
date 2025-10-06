package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), HomeFragment.OnAlbumButtonClickListener {
    private lateinit var binding : ActivityMainBinding
    companion object {const val STRING_INTENT_KEY = "title"}
    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == RESULT_OK) {
            val returnString = result.data?.getStringExtra(STRING_INTENT_KEY)
            val toast = Toast.makeText(this, returnString, Toast.LENGTH_SHORT) // in Activity
            toast.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, HomeFragment())
            .commit()

        binding.songButton.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", binding.miniTitle.text.toString())
            intent.putExtra("singer", binding.miniSinger.text.toString())
            getResultText.launch(intent)
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