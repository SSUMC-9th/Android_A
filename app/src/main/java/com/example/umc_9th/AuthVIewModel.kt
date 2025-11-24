package com.example.umc_9th

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    //로그인 한 결과 얻는 값들 여기에
    var memberId: Int = 0
    var accessToken: String = ""
    var nickname: String = ""


    //로그인 결과 추적
    private val _loginResult = MutableLiveData<Result<LoginData>>()
    val loginResult: LiveData<Result<LoginData>> = _loginResult

    //회원가입 결과 추적
    private val _signupResult = MutableLiveData<Result<SignData>>()
    val signupResult: LiveData<Result<SignData>> = _signupResult

    //JWT 결과 추적
    private val _testResult = MutableLiveData<Result<TestData>>()
    val testResult: LiveData<Result<TestData>> = _testResult


    //유저 정보 수정 결과 추적
    private val _changeResult = MutableLiveData<Result<ChangeData>>()
    val changeResult: LiveData<Result<ChangeData>> = _changeResult

    //유저 정보 삭제 결과 추적
    private val _deleteResult = MutableLiveData<Result<Unit>>()
    val deleteResult: LiveData<Result<Unit>> = _deleteResult


    /**실제로 클라이언트가 실행하는 건 이거 -> 코루틴으로 별도 쓰레드**/
    //로그인
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val request = LoginRequest(email, password)
            val result = repository.Login(request)
            _loginResult.postValue(result)
        }
    }

    //회원가입
    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            val request = SignRequest(name, email, password)
            val result = repository.Signup(request)
            _signupResult.postValue(result)
        }
    }

    //헤더 테스트
    fun testJWT(token: String) {
        viewModelScope.launch {
            val result = repository.testJWT(token)
            _testResult.postValue(result)
        }
    }

    //유저 정보 수정
    fun changeInfo(token: String, memberId: Int, newName: String, newPassword: String) {
        viewModelScope.launch {
            val request = ChangeRequest(memberId, newName, newPassword)
            val result = repository.changeInfo(token, request)
            _changeResult.postValue(result)
        }
    }

    //유저 정보 삭제(회원탈퇴)
    fun deleteUser(token: String, memberId: Int, password: String) {
        viewModelScope.launch {
            val result = repository.deleteUser(token, memberId, password)
            _deleteResult.postValue(result)
        }
    }
}