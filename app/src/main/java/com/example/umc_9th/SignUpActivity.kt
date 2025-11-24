package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    //MainActivity처럼 ViewModel 정의
    private val authyRepository by lazy {
        AuthRepository(ApiClient.authService)
    }
    private val authViewModel: AuthViewModel by viewModels(
        factoryProducer = { AuthViewModelFactory(authyRepository) }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeSignup()
        binding.signinBtn.setOnClickListener {
            if(binding.idToSignup.text.toString() != "") {
                if(binding.pwdToSignup.text.toString() == binding.pwdVerify.text.toString()) {
                    CreateNewAccount(binding.idToSignup.text.toString(),binding.pwdToSignup.text.toString(), binding.nicknameToSignup.text.toString())
                }
            }
        }
        binding.lookPwda.setOnClickListener {
            if(binding.pwdToSignup.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                binding.pwdToSignup.inputType = InputType.TYPE_CLASS_TEXT
                binding.lookPwda.setImageResource(R.drawable.btn_input_password_off)
            }
            else {
                binding.pwdToSignup.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.lookPwda.setImageResource(R.drawable.btn_input_password)
            }
        }
        binding.lookPwdb.setOnClickListener {
            if(binding.pwdVerify.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                binding.pwdVerify.inputType = InputType.TYPE_CLASS_TEXT
                binding.lookPwdb.setImageResource(R.drawable.btn_input_password_off)
            }
            else {
                binding.pwdVerify.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.lookPwdb.setImageResource(R.drawable.btn_input_password)
            }
        }

    }

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
    fun CreateNewAccount(id : String, password : String, nickname : String) {
        if (id.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        authViewModel.signup(nickname, id, password)
        finish()
    }
}

