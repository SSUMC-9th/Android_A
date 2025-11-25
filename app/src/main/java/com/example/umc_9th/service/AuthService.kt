package com.example.umc_9th.service

import com.example.umc_9th.data.AuthResponse
import com.example.umc_9th.data.ChangeRequest
import com.example.umc_9th.data.ChangeResultData
import com.example.umc_9th.data.LoginRequest
import com.example.umc_9th.data.LoginResultData
import com.example.umc_9th.data.SignUpRequest
import com.example.umc_9th.data.SignUpResultData
import com.example.umc_9th.data.TokenTestResultData
import com.example.umc_9th.data.WithdrawRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthService {

    //회원가입
    @POST("signup")
    suspend fun signUp(
        @Body req: SignUpRequest
    ) : Response<AuthResponse<SignUpResultData>>

    //로그인
    @POST("login")
    suspend fun login(
        @Body req: LoginRequest
    ) : Response<AuthResponse<LoginResultData>>

    //test
    @GET("test")
    suspend fun testJWT(
        @Header("Authorization") token: String,
    ) : Response<AuthResponse<TokenTestResultData>>

    //유저 정보 수정
    @PATCH("users")
    suspend fun changeInfo(
        @Header("Authorization") token: String,
        @Body req: ChangeRequest
    ) : Response<AuthResponse<ChangeResultData>>

    //유저 정보 삭제
    @HTTP(method = "DELETE", path = "users", hasBody = true)
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Body req: WithdrawRequest
    ) : Response<AuthResponse<String>>

}