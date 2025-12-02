package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umc_9th.network.NetworkManager
import com.example.umc_9th.request.LoginRequest
import kotlinx.coroutines.launch
import umc.study.umc_8th.R

class LoginActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var emailPrefixEt: EditText
    private lateinit var emailDomainEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button
    private lateinit var toSignUpTv: TextView
    private lateinit var passwordToggle: ImageView
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authManager = AuthManager.getInstance(this)

        // 이미 로그인된 경우 MainActivity로 이동
        if (authManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        emailPrefixEt = findViewById(R.id.login_email_et1)
        emailDomainEt = findViewById(R.id.login_email_et2)
        passwordEt = findViewById(R.id.login_password_et)
        passwordToggle = findViewById(R.id.login_password_toggle)
        loginBtn = findViewById(R.id.login_btn)
        toSignUpTv = findViewById(R.id.login_to_signup_tv)

        passwordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                // 비밀번호 보이기
                passwordEt.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordToggle.setImageResource(R.drawable.btn_input_password_off)
            } else {
                // 비밀번호 숨기기
                passwordEt.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordToggle.setImageResource(R.drawable.btn_input_password)
            }
            //passwordEt.setSelection(passwordEt.text.length)
        }

        loginBtn.setOnClickListener {
            val emailPrefix = emailPrefixEt.text.toString().trim()
            val emailDomain = emailDomainEt.text.toString().trim()
            val email = "$emailPrefix@$emailDomain"
            val password = passwordEt.text.toString().trim()

            when {
                emailPrefix.isEmpty() -> {
                    Toast.makeText(this, "이메일 아이디를 입력하세요", Toast.LENGTH_SHORT).show()
                }
                emailDomain.isEmpty() -> {
                    Toast.makeText(this, "이메일 도메인을 입력하세요", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    signIn(email, password)
                }
            }
        }

        toSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun signIn(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = NetworkManager.apiService.login(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == true) {

                        // jwt 토큰이 없어 response 헤더에서 가져오기
                        val token = response.headers()["Authorization"] ?: ""


                        if (token.isNotEmpty()) {
                            authManager.saveAccessToken(token)
                            authManager.saveUserToPrefs("unknown", email)  // userIdx가 없음

                            Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()

                            // Test API 호출
                            testApi(token)

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "토큰을 받지 못했습니다", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패: ${body?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun testApi(token: String) {
        lifecycleScope.launch {
            try {
                val response = NetworkManager.apiService.test("Bearer $token")

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == true) {
                        Toast.makeText(this@LoginActivity, "Test API: ${body.data?.result}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // 실패해도 무시
            }
        }
    }

    /*
    private fun signIn(email: String, password: String) {
        authManager.signIn(
            email = email,
            password = password,
            onSuccess = { user ->
                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },
            onFailure = { error ->
                Toast.makeText(this, "로그인 실패: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
     */
}