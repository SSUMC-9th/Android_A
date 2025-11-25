package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_8th.R

class LoginActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var emailPrefixEt: EditText
    private lateinit var emailDomainEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button
    private lateinit var toSignUpTv: TextView

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

        // 🔥 XML ID와 매칭
        emailPrefixEt = findViewById(R.id.login_email_et1)
        emailDomainEt = findViewById(R.id.login_email_et2)
        passwordEt = findViewById(R.id.login_password_et)
        loginBtn = findViewById(R.id.login_btn)
        toSignUpTv = findViewById(R.id.login_to_signup_tv)

        loginBtn.setOnClickListener {
            val emailPrefix = emailPrefixEt.text.toString().trim()
            val emailDomain = emailDomainEt.text.toString().trim()
            val email = "$emailPrefix@$emailDomain"  // 🔥 이메일 조합
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
}