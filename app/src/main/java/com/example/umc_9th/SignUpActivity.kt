package com.example.umc_9th

import android.os.Bundle
import android.text.InputType
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val userRef = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var binding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signinBtn.setOnClickListener {
            if(binding.idToSignup.text.toString() != "") {
                if(binding.pwdToSignup.text.toString() == binding.pwdVerify.text.toString()) {
                    CreateNewAccount(binding.idToSignup.text.toString(),binding.pwdToSignup.text.toString() )
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
    fun CreateNewAccount(id : String, password : String) {
        userRef.child("sampleID").child("id").setValue(id)
        userRef.child("sampleID").child("password").setValue(password)
        finish()
    }
}