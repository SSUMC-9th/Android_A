package com.example.umc_9th.access

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umc_9th.data.ChangeRequest
import com.example.umc_9th.data.ChangeResultData
import com.example.umc_9th.data.LoginRequest
import com.example.umc_9th.data.LoginResultData
import com.example.umc_9th.data.SignUpRequest
import com.example.umc_9th.data.SignUpResultData
import com.example.umc_9th.data.TokenTestResultData
import com.example.umc_9th.data.WithdrawRequest
import com.example.umc_9th.repository.AuthRepository

import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    //로그인 한 결과 얻는 값들 여기에
    var memberId: Int = 0
    var accessToken: String = ""
    var nickname: String = ""


    //로그인 결과 추적
    private val _loginResult = MutableLiveData<Result<LoginResultData>>()
    val loginResult: LiveData<Result<LoginResultData>> = _loginResult

    //회원가입 결과 추적
    private val _signupResult = MutableLiveData<Result<SignUpResultData>>()
    val signupResult: LiveData<Result<SignUpResultData>> = _signupResult

    //JWT 결과 추적
    private val _testResult = MutableLiveData<Result<TokenTestResultData>>()
    val testResult: LiveData<Result<TokenTestResultData>> = _testResult


    //유저 정보 수정 결과 추적
    private val _changeResult = MutableLiveData<Result<ChangeResultData>>()
    val changeResult: LiveData<Result<ChangeResultData>> = _changeResult

    //유저 정보 삭제 결과 추적
    private val _deleteResult = MutableLiveData<Result<String>>()
    val deleteResult: LiveData<Result<String>> = _deleteResult


    /**실제로 클라이언트가 실행하는 건 이거 -> 코루틴으로 별도 쓰레드**/
    //로그인
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val request = LoginRequest(email, password)
            val result = repository.login(request)
            _loginResult.postValue(result)
        }
    }

    //회원가입
    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            val request = SignUpRequest(name, email, password)
            val result = repository.signUp(request)
            _signupResult.postValue(result)
        }
    }

    //헤더 테스트
    fun testJWT(token: String){
        viewModelScope.launch {
            val result = repository.testJWT(token)
            _testResult.postValue(result)
        }
    }

    //유저 정보 수정
    fun changeInfo(token: String, memberId: Int, newName: String, newPassword: String){
        viewModelScope.launch {
            val request = ChangeRequest(memberId, newName, newPassword)
            val result = repository.changeInfo(token, request)
            _changeResult.postValue(result)
        }
    }

    //유저 정보 삭제(회원탈퇴)
    fun deleteUser(token: String, memberId: Int, password: String){
        viewModelScope.launch {
            val request = WithdrawRequest(memberId, password)
            repository.deleteUser(token, request)
            val result = repository.deleteUser(token, request)
            _deleteResult.postValue(result)
        }
    }
}