package com.example.umc_9th

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.example.umc_9th.access.AuthViewModel
import com.example.umc_9th.access.AuthViewModelFactory
import com.example.umc_9th.network.RetrofitClient
import com.example.umc_9th.repository.AuthRepository
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding

    //MainActivity처럼 ViewModel 정의
    private val authyRepository by lazy {
        AuthRepository(RetrofitClient.authService)
    }
    private val authViewModel: AuthViewModel by viewModels(
        factoryProducer = { AuthViewModelFactory(authyRepository) }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeSignup()

        //EditText 값이 변경될 때마다 체크
        listOf(
            binding.etEmailId, binding.etEmailDomain,
            binding.etPassword, binding.etPasswordConfirm,
            binding.etNickname
        ).forEach { editText ->
            editText.doAfterTextChanged {
                validInputcheck()
            }
        }

        binding.btnRegister.setOnClickListener {
            signUserInfoWithApi()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //회원가입 API 결과 observe
    private fun observeSignup() {
        authViewModel.signupResult.observe(this) { result ->
            result.onSuccess { data ->
                Toast.makeText(this, "회원가입 성공! 회원 ID: ${data.memberId}", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            result.onFailure { error ->
                Toast.makeText(this, "회원가입 실패: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    //회원가입 진행
    private fun signUserInfoWithApi() {
        var id = binding.etEmailId.text.toString().trim()
        var domain = binding.etEmailDomain.text.toString().trim()
        var pw = binding.etPassword.text.toString().trim()
        var emailid = "$id@$domain"
        var nickname = binding.etNickname.text.toString().trim()

        authViewModel.signup(nickname, emailid, pw)
    }

    //값이 유효한지 체크(비밀번호 몇자리 이상)
    private fun validInputcheck() {
        var id = binding.etEmailId.text.toString().trim()
        var domain = binding.etEmailDomain.text.toString().trim()
        var pw = binding.etPassword.text.toString().trim()
        var pwCheck = binding.etPasswordConfirm.text.toString().trim()
        var nickname = binding.etNickname.text.toString().trim()

        //비밀번호 check
        if (pw.length < 6) {
            binding.etError.text = "비밀번호는 최소 6자리 이상이어야 합니다."
            binding.etError.visibility = TextView.VISIBLE
        } else if (pwCheck.length >= 1 && pw != pwCheck) {
            binding.etError.text = "비밀번호가 일치하지 않습니다."
            binding.etError.visibility = TextView.VISIBLE
        } else {
            binding.etError.visibility = TextView.INVISIBLE
        }

        var isValid = false
        if (!id.isEmpty() && !domain.isEmpty() && pw.length >= 6 && pw == pwCheck && !nickname.isEmpty()) {
            isValid = true
        }

        if (isValid) {
            binding.btnRegister.isEnabled = true
            binding.btnRegister.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#3E42FF"))
        } else {
            binding.btnRegister.isEnabled = false
            binding.btnRegister.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#919194"))
        }

    }

}