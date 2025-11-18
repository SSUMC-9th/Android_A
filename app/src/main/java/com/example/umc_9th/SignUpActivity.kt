package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_8th.R

class SignUpActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var emailPrefixEt: EditText  // 🔥 수정
    private lateinit var emailDomainEt: EditText  // 🔥 추가
    private lateinit var passwordEt: EditText     // 🔥 수정
    private lateinit var passwordCheckEt: EditText // 🔥 수정
    private lateinit var signUpBtn: Button
    private lateinit var toLoginTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        authManager = AuthManager.getInstance(this)

        emailPrefixEt = findViewById(R.id.signup_email_et1)
        emailDomainEt = findViewById(R.id.signup_email_et2)
        passwordEt = findViewById(R.id.signup_password_et)
        passwordCheckEt = findViewById(R.id.signup_password_check_et)
        signUpBtn = findViewById(R.id.signup_btn)
        toLoginTv = findViewById(R.id.signup_to_login_tv)

        signUpBtn.setOnClickListener {
            val emailPrefix = emailPrefixEt.text.toString().trim()
            val emailDomain = emailDomainEt.text.toString().trim()
            val email = "$emailPrefix@$emailDomain"
            val password = passwordEt.text.toString().trim()
            val passwordCheck = passwordCheckEt.text.toString().trim()

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
                password.length < 6 -> {
                    Toast.makeText(this, "비밀번호는 6자 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                }
                passwordCheck.isEmpty() -> {
                    Toast.makeText(this, "비밀번호 확인을 입력하세요", Toast.LENGTH_SHORT).show()
                }
                password != passwordCheck -> {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    signUp(email, password)
                }
            }
        }

        toLoginTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun signUp(email: String, password: String) {
        authManager.signUp(
            email = email,
            password = password,
            onSuccess = { user ->
                Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },
            onFailure = { error ->
                Toast.makeText(this, "회원가입 실패: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
}