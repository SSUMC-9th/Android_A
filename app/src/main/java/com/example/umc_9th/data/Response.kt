package com.example.umc_9th.data

// Response\
data class AuthResponse<T>(
    val status: Boolean,
    val code: String,
    val message: String,
    val data: T? = null
)

// Request

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)

data class ChangeRequest(
    val memberId: Int,
    val newName: String,
    val newPassword: String
)

data class WithdrawRequest(
    val memberId: Int,
    val password: String
)

// ResultData

data class LoginResultData(
    val name: String,
    val memberId: Int,
    val accessToken: String
)

data class TokenTestResultData(
    val result: String
)

data class ChangeResultData(
    val memberId: Int?
)

data class SignUpResultData (
    val memberId: Int
)
