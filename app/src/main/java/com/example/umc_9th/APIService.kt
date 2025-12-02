package com.example.umc_9th

import com.example.umc_9th.request.LoginRequest
import com.example.umc_9th.request.SignUpRequest
import com.example.umc_9th.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // 회원가입
    @POST("signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<BaseResponse<SignUpResponse>>

    // 로그인
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<BaseResponse<LoginResponse>>

    // Test API
    @GET("test")
    suspend fun test(
        @Header("Authorization") authorization: String
    ): Response<BaseResponse<TestResponse>>
}