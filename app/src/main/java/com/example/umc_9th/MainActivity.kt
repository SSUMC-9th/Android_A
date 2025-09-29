package com.example.umc_9th

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import umc.study.umc_8th.databinding.ActivityMainBinding
import umc.study.umc_8th.R

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.face1.setOnClickListener {
            binding.explanation1.setTextColor(ContextCompat.getColor(this, R.color.blue_500))
            Toast.makeText(this, "행복 버튼을 클릭했습니다", Toast.LENGTH_LONG).show()
        }

        binding.face2.setOnClickListener {
            binding.explanation2.setTextColor(ContextCompat.getColor(this, R.color.blue_500))
            Toast.makeText(this, "흥분 버튼을 클릭했습니다", Toast.LENGTH_LONG).show()
        }

        binding.face3.setOnClickListener {
            binding.explanation3.setTextColor(ContextCompat.getColor(this, R.color.blue_500))
            Toast.makeText(this, "평범 버튼을 클릭했습니다", Toast.LENGTH_LONG).show()
        }

        binding.face4.setOnClickListener {
            binding.explanation4.setTextColor(ContextCompat.getColor(this, R.color.blue_500))
            Toast.makeText(this, "생각 버튼을 클릭했습니다", Toast.LENGTH_LONG).show()
        }

        binding.face5.setOnClickListener {
            binding.explanation5.setTextColor(ContextCompat.getColor(this, R.color.blue_500))
            Toast.makeText(this, "분노 버튼을 클릭했습니다", Toast.LENGTH_LONG).show()
        }
    }
}