package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ActivityLoginBinding
import umc.study.umc_9th.databinding.ActivitySignUpBinding
import kotlin.getValue

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val authyRepository by lazy {
        AuthRepository(ApiClient.authService)
    }
    private val authViewModel: AuthViewModel by viewModels(
        factoryProducer = { AuthViewModelFactory(authyRepository) }
    )
    private var tokentemp : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeLogin()
        observeJWT()
        binding.loginButton.setOnClickListener {
            val inpId = binding.idToLogin.text.toString()
            val inpPwd = binding.PwdToLogin.text.toString()
            Login(inpId, inpPwd)

        }
        binding.lookPwd.setOnClickListener {
            if(binding.PwdToLogin.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                binding.PwdToLogin.inputType = InputType.TYPE_CLASS_TEXT
                binding.lookPwd.setImageResource(R.drawable.btn_input_password_off)
            }
            else {
                binding.PwdToLogin.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.lookPwd.setImageResource(R.drawable.btn_input_password)
            }
        }
        binding.gotoSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
    private fun observeLogin() {
        authViewModel.loginResult.observe(this) { result ->
            //Result -> status, code 등이 있고 이 안 data에 값이 존재
            result.onSuccess { data ->
                Toast.makeText(this, "로그인 성공! 회원 ID: ${data.memberId}", Toast.LENGTH_LONG).show()
                authViewModel.memberId = data.memberId
                authViewModel.accessToken = data.accessToken
                authViewModel.nickname = data.name
                tokentemp = data.accessToken
            }.onFailure { error ->
                val message = error.message ?: "알 수 없는 오류가 발생했습니다."
                Log.d("tag", "로그인 실패: $message")
                Toast.makeText(this, "로그인 실패: $message", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun observeJWT() {
        authViewModel.testResult.observe(this) { result ->
            //Result -> status, code 등이 있고 이 안 data에 값이 존재
            result.onSuccess { data, ->
                Toast.makeText(this, data.result, Toast.LENGTH_LONG).show()
            }.onFailure { error ->
                val message = error.message ?: "알 수 없는 오류가 발생했습니다."
                Log.d("tag", "test failure: $message")
                Toast.makeText(this, "test failure: $message", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun Login(id : String, pw : String) {
        if (id.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        authViewModel.login(id, pw)
        authViewModel.testJWT(tokentemp)
        finish();
    }
}
