package com.example.umc_9th

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.umc_9th.access.AuthViewModel
import com.example.umc_9th.access.AuthViewModelFactory
import com.example.umc_9th.network.RetrofitClient
import com.example.umc_9th.repository.AuthRepository
import umc.study.umc_8th.databinding.ActivityLoginBinding


class LoginActivity: AppCompatActivity(){
    lateinit var binding: ActivityLoginBinding
    private lateinit var userDB: AppDatabase

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //userDB = AppDatabase.getInstance(this)

        // ViewModel 초기화 (팩토리 패턴 사용)
        val authService = RetrofitClient.authService
        val repository = AuthRepository(authService)
        val factory = AuthViewModelFactory(repository)

        // 뷰모델 생성
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        //liveData 관찰
        observeLogin()

        //로그인
        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    //liveData 관찰 (API 결과를 받으면 처리하는 로직)
    private fun observeLogin() {
        authViewModel.loginResult.observe(this) { result ->
            //Result -> status, code 등이 있고 이 안 data에 값이 존재
            result.onSuccess { data ->
                Toast.makeText(this, "로그인 성공! 회원 ID: ${data.memberId}", Toast.LENGTH_LONG).show()
                authViewModel.memberId = data.memberId
                authViewModel.accessToken = data.accessToken
                authViewModel.nickname = data.name

                // 토큰 저장 (자동 로그인을 위해 SharedPreference에 저장)
                saveLoginStatus(data.accessToken, data.memberId, data.name)

            }.onFailure { error ->
                val message = error.message ?: "알 수 없는 오류가 발생했습니다."
                Log.d("tag", "로그인 실패: $message")
                Toast.makeText(this, "로그인 실패: $message", Toast.LENGTH_LONG).show()
            }
        }
    }

    //API 호출
    private fun performLogin(){
        var id = binding.etEmailId.text.toString().trim()
        var email = binding.etEmailDomain.text.toString().trim()
        var pw = binding.etPassword.text.toString().trim()

        var fullEmail = "$id@$email"

        if (id.isEmpty() || email.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        //ViewModel에서 API 호출 -> 그 결과는 observe로 처리
        authViewModel.login(fullEmail, pw)
    }

    private fun saveLoginStatus(token: String, memberId: Int, name: String) {
        val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPref.edit().apply {
            putBoolean("isLoggedIn", true)
            putString("accessToken", token)
            putInt("memberId", memberId)
            putString("userName", name)
            apply()
        }
    }
}
