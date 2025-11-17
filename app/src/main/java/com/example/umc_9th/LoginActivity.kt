package com.example.umc_9th.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_9th.MainActivity
import com.example.umc_9th.data.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import umc.study.umc_8th.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase 초기화
        auth = Firebase.auth
        database = Firebase.database.reference

        // [자동 로그인] 앱 켤 때 저장된 ID가 있으면 바로 메인으로 이동
        if (getUserIdFromSharedPref() != null) {
            startMainActivity()
        }

        binding.tvSignUp.setOnClickListener { signUp() }
        binding.btnLogin.setOnClickListener { login() }
    }

    // 1. 회원가입: Auth 계정 생성 -> DB에 유저 정보 저장 -> SharedPref에 ID 저장
    private fun signUp() {
        val email = binding.etEmailId.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid

                        // ✅ [미션 1] User 데이터를 DB에 추가
                        val user = User(uid, email, "사용자") // 이름은 임시로 설정
                        database.child("Users").child(uid).setValue(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                                // ✅ [미션 2] SharedPreference에 ID 저장 후 이동
                                saveUserIdToSharedPref(uid)
                                startMainActivity()
                            }
                    }
                } else {
                    Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // 2. 로그인: Auth 로그인 -> SharedPref에 ID 저장
    private fun login() {
        val email = binding.etEmailId.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) return

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                        // ✅ [미션 2] SharedPreference에 ID 저장 후 이동
                        saveUserIdToSharedPref(user.uid)
                        startMainActivity()
                    }
                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // SharedPreferences에 UserID 저장하는 함수
    private fun saveUserIdToSharedPref(uid: String) {
        val sharedPref = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_id", uid)
            apply()
        }
    }

    // SharedPreferences에서 UserID 가져오는 함수
    private fun getUserIdFromSharedPref(): String? {
        val sharedPref = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("user_id", null)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}