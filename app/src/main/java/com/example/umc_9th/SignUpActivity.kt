package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umc_9th.network.NetworkManager
import com.example.umc_9th.request.SignUpRequest
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import umc.study.umc_8th.R

class SignUpActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var emailPrefixEt: EditText
    private lateinit var emailDomainEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var passwordCheckEt: EditText
    private lateinit var nameEt: EditText
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
        nameEt = findViewById(R.id.signup_name_et)
        signUpBtn = findViewById(R.id.signup_btn)
        toLoginTv = findViewById(R.id.signup_to_login_tv)

        signUpBtn.setOnClickListener {
            val emailPrefix = emailPrefixEt.text.toString().trim()
            val emailDomain = emailDomainEt.text.toString().trim()
            val email = "$emailPrefix@$emailDomain"
            val password = passwordEt.text.toString().trim()
            val passwordCheck = passwordCheckEt.text.toString().trim()
            val name = nameEt.text.toString().trim()

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
                name.isEmpty() -> {
                    Toast.makeText(this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    signUp(email, password, name)
                }
            }
        }

        toLoginTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun signUp(email: String, password: String, name: String) {
        lifecycleScope.launch {
            try {
                val request = SignUpRequest(email, password, name)
                val response = NetworkManager.apiService.signUp(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == true) {
                        saveToFirebase(email, password)

                        Toast.makeText(this@SignUpActivity, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity, "회원가입 실패: ${body?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SignUpActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveToFirebase(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Firebase 가입 성공 (좋아요 기능 사용 가능)
            }
            .addOnFailureListener {
                // Firebase 가입 실패해도 서버 가입은 성공했으므로 무시
            }
    }
}