package com.example.umc_9th
data class AuthResponse<T>(
    val status : String,
    val code : String,
    val message : String,
    val data : T? = null
)
data class SignRequest(
    val name : String,
    val email : String,
    val password : String
)

data class SignData(
    val memberId: Int
)

data class LoginRequest(
    val email : String,
    val password : String
)
data class LoginData(
    val name: String,
    val memberId: Int,
    var accessToken : String
)

data class TestData(
    val result: String
)

data class ChangeRequest(
    val memberId: Int,
    val newName : String,
    val newPassword : String
)
data class ChangeData(
    val memberId: Int
)