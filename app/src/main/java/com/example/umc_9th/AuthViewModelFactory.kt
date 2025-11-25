package com.example.umc_9th

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AuthViewModelFactory(private val repository: AuthRepository) :
    ViewModelProvider.Factory  {
    //요청한 ViewModel 클래스가 AuthViewModel과 같거나 상속 관계인지 확인한다.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}