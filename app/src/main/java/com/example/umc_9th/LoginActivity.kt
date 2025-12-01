package com.example.umc_9th

import android.content.Intent
import android.graphics.BitmapFactory
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
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ActivityLoginBinding
import umc.study.umc_9th.databinding.ActivitySignUpBinding
import java.net.URL
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
        val keyHash = com.kakao.sdk.common.util.Utility.getKeyHash(this)
        Log.d("KeyHash", keyHash)
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
        binding.kakaoLogin.setOnClickListener {
            // 로그인 조합 예제

// 카카오계정으로 로그인 공통 callback 구성
// 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("TAG", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
                }
            }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("TAG", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i("TAG", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        updateKakaoData()

                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
        binding.naverLogin.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("TAG", "로그아웃 실패. SDK에서 토큰 폐기됨", error)
                }
                else {
                    Log.i("TAG", "로그아웃 성공. SDK에서 토큰 폐기됨")
                }
            }
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
    private fun updateKakaoData() {
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("TAG", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i("TAG", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                Thread {
                    val url = URL(user.kakaoAccount?.profile?.thumbnailImageUrl)
                    val bitmap = BitmapFactory.decodeStream(url.openStream())

                    runOnUiThread {
                        binding.kakaoImg.setImageBitmap(bitmap)
                    }
                }.start()
                binding.nickname.text = user.kakaoAccount?.profile?.nickname + " " + user.id
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
