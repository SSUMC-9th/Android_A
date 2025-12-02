package com.example.umc_9th.response

data class BaseResponse<T>(
    val status: Boolean,
    val code: String,
    val message: String,
    val data: T?
)