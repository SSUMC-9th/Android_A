package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

class LoginActivity : AppCompatActivity() {
    private val userRef = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val inpId = binding.idToLogin.text.toString()
            var inpPwd = binding.PwdToLogin.text.toString()
            val query = userRef.orderByChild("id").equalTo(inpId)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            if(child.child("password").value == inpPwd) Login(child.key.toString())
                            else Toast.makeText(applicationContext, "사용자 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "사용자 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("RealtimeDB", "쿼리 실패", error.toException())
                }
            })

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
    fun Login(id : String) {
        val uidStorage = loginStorage(this)
        uidStorage.setUid(id)
        finish();
    }
}
