package com.android.myapplication.api

import com.android.myapplication.dto.DiscResultResponse
import com.android.myapplication.dto.DiscUsers
import com.android.myapplication.dto.ResponseObject
import com.android.myapplication.ui.disc.data_class.DiscTestResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

// API 인터페이스 정의
interface ApiService {
    @GET("/login")
    suspend fun login(@Query("loginId") loginId: String, @Query("password") password: String): ResponseObject

    @GET("/user")
    suspend fun myPage(@Header("Authorization") Authorization: String): ResponseObject

    @POST("/DISC")
    suspend fun saveDiscTestResult(
        @Header("Authorization") token: String,
        @Body discTestResult: DiscTestResult
    ): DiscResultResponse

    @GET("/DISC")
    suspend fun getDiscResult(
        @Header("Authorization") token: String
    ): DiscResultResponse

    @GET("/DISC_headcount")
    suspend fun getDiscUsers(
        @Header("Authorization") token: String
    ) : DiscUsers

}