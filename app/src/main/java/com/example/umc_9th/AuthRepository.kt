package com.example.umc_9th

import android.util.Log

class AuthRepository(private val service: AuthService) {

    //1.로그인
    suspend fun Login(
        req: LoginRequest
    ): Result<LoginData> = try {
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

    //2.회원가입
    suspend fun Signup(
        req: SignRequest
    ): Result<SignData> = try {
        val response = service.signUp(req)

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
            Result.failure(RuntimeException("HTTP ${response.code()}: $errMsg"))
        }

    } catch (e: Exception) {
        //오류
        Result.failure(e)

    }

    //3. JWT 테스트
    suspend fun testJWT(accessToken: String)
            : Result<TestData> = try {
        //헤더를 넣는다.
        val token = if (accessToken.startsWith("Bearer ")) accessToken else "Bearer $accessToken"
        val response = service.testJWT(token)

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
            Result.failure(RuntimeException("HTTP ${response.code()}: $errMsg"))
        }
    } catch (e: Exception) {
        //오류
        Result.failure(e)

    }

    //4. 유저 정보 수정(둘 다 값을 넣어야 한다)
    suspend fun changeInfo(accessToken: String, req: ChangeRequest)
            : Result<ChangeData> = try {
        val token = if (accessToken.startsWith("Bearer ")) accessToken else "Bearer $accessToken"
        val response = service.changeInfo(token, req)

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
            Result.failure(RuntimeException("HTTP ${response.code()}: $errMsg"))
        }

    } catch (e: Exception) {
        //오류
        Result.failure(e)

    }

    //5. 유저 정보 삭제
    suspend fun deleteUser(accessToken: String, memberId: Int, password: String)
            : Result<Unit> = try {
        val token = if (accessToken.startsWith("Bearer ")) accessToken else "Bearer $accessToken"
        val response = service.deleteUser(token, memberId, password)

        //성공 리턴 시
        if (response.isSuccessful) {
            val body = response.body()
            //body가 없으면
            Result.success(Unit)
        }
        //잘못된 리턴(200X 오류 등)
        else {
            val errMsg = response.errorBody()?.string() ?: response.message()
            Result.failure(RuntimeException("HTTP ${response.code()}: $errMsg"))
        }

    } catch (e: Exception) {
        //오류
        Result.failure(e)

    }
}
