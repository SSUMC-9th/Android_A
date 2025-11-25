package com.example.umc_9th.repository

import android.util.Log
import com.example.umc_9th.data.ChangeRequest
import com.example.umc_9th.data.ChangeResultData
import com.example.umc_9th.data.LoginRequest
import com.example.umc_9th.data.LoginResultData
import com.example.umc_9th.data.SignUpRequest
import com.example.umc_9th.data.SignUpResultData
import com.example.umc_9th.data.TokenTestResultData
import com.example.umc_9th.data.WithdrawRequest
import com.example.umc_9th.service.AuthService


class AuthRepository(private val service: AuthService) {

    //1.로그인
    suspend fun login(
        req: LoginRequest
    ): Result<LoginResultData> = try {
        val response = service.login(req)

        //성공 리턴 시
        if (response.isSuccessful) {
            val body = response.body()
            //body가 없으면
            if (body == null) {
                Log.d("tag", "Response body is null")
                Result.failure(RuntimeException("Response body is null"))
            }
            //data 값이 없을 때
            else if (body.data == null) {
                Log.d("tag", "Response OK but Data is null")
                Result.failure(RuntimeException("Response OK but Data is null"))
            } else {
                Log.d("tag", "OK")
                Result.success(body.data)
            }
        }
        //잘못된 리턴(200X 오류 등)
        else {
            val errMsg = response.errorBody()?.string() ?: response.message()
            Log.d("tag", "비상: $errMsg")
            Result.failure(RuntimeException("HTTP ${response.code()}: $errMsg"))
        }
    } catch (e: Exception) {
        //오류
        Result.failure(e)
    }

    // 2. 회원가입
    suspend fun signUp(req: SignUpRequest): Result<SignUpResultData> = try {
        val response = service.signUp(req)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(RuntimeException("Data is null"))
            }
        } else {
            val errMsg = response.errorBody()?.string() ?: response.message()
            Result.failure(RuntimeException("HTTP ${response.code()}: $errMsg"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 3. 토큰 테스트
    suspend fun testJWT(token: String): Result<TokenTestResultData> = try {
        val response = service.testJWT(token)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(RuntimeException("Data is null"))
            }
        } else {
            Result.failure(RuntimeException("Error: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 4. 정보 수정
    suspend fun changeInfo(token: String, req: ChangeRequest): Result<ChangeResultData> = try {
        val response = service.changeInfo(token, req)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(RuntimeException("Data is null"))
            }
        } else {
            Result.failure(RuntimeException("Error: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 5. 회원 탈퇴
    // 이 부분은 data가 String 혹은 null일 수 있으므로 유연하게 처리합니다.
    suspend fun deleteUser(token: String, req: WithdrawRequest): Result<String> = try {
        val response = service.deleteUser(token, req)
        if (response.isSuccessful) {
            val body = response.body()
            // 탈퇴는 data가 null이어도 성공("성공적으로 삭제되었습니다") 메시지만 있으면 성공으로 처리
            Result.success(body?.message ?: "Unknown Success")
        } else {
            val errMsg = response.errorBody()?.string() ?: response.message()
            Result.failure(RuntimeException("HTTP ${response.code()}: $errMsg"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}