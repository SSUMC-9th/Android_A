package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
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
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch
import com.example.umc_9th.util.loginWithKakao

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authService = RetrofitClient.authService
        val repository = AuthRepository(authService)
        val factory = AuthViewModelFactory(repository)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        observeLogin()

        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnKakao.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val token = UserApiClient.loginWithKakao(this@LoginActivity)

                    Log.i("KAKAO", "로그인 성공! 토큰: ${token.accessToken}")

                    Prefs.setToken(this@LoginActivity, token.accessToken)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } catch (e: Exception) {
                    Log.e("KAKAO", "로그인 실패", e)
                    Toast.makeText(this@LoginActivity, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeLogin() {
        authViewModel.loginResult.observe(this) { result ->
            result.onSuccess { data ->
                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()

                // 데이터 저장
                authViewModel.memberId = data.memberId
                authViewModel.accessToken = data.accessToken
                authViewModel.nickname = data.name
                Prefs.setToken(this, data.accessToken)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }.onFailure { error ->
                val message = error.message ?: "오류가 발생했습니다."
                Toast.makeText(this, "로그인 실패: $message", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun performLogin() {
        val id = binding.etEmailId.text.toString().trim()
        val domain = binding.etEmailDomain.text.toString().trim()
        val pw = binding.etPassword.text.toString().trim()

        if (id.isEmpty() || domain.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val fullEmail = "$id@$domain"
        authViewModel.login(fullEmail, pw)
    }
}