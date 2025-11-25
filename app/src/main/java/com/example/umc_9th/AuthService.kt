package com.example.umc_9th

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("/signup")
    suspend fun signUp(
        @Body req: SignRequest
    ) : Response<AuthResponse<SignData>>

    @POST("/login")
    suspend fun login(
        @Body req: LoginRequest
    ) : Response<AuthResponse<LoginData>>

    @GET("/test")
    suspend fun testJWT(
        @Header("Authorization") token: String,
    ) : Response<AuthResponse<TestData>>

    @PATCH("/users")
    suspend fun changeInfo(
        @Header("Authorization") token: String,
        @Body req: ChangeRequest
    ) : Response<AuthResponse<ChangeData>>

    @DELETE("/users")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Query("memberId") memberId: Int,
        @Query("password") password: String
    ) : Response<AuthResponse<Unit>>




}