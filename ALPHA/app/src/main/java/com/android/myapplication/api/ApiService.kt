package com.android.myapplication.api

import com.android.myapplication.dto.ResponseObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// API 인터페이스 정의
interface ApiService {
    @GET("/login")
    suspend fun login(@Query("loginId") loginId: String, @Query("password") password: String): ResponseObject

    @GET("/user")
    suspend fun myPage(@Header("Authorization") Authorization: String): ResponseObject

}