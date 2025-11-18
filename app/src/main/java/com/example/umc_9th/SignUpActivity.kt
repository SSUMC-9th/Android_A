package com.example.umc_9th

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_8th.databinding.ActivitySignUpBinding

class SignUpActivity: AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}