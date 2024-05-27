package com.android.myapplication.api

import com.android.myapplication.dto.EditProfile
import com.android.myapplication.dto.ResponseObject
import com.android.myapplication.dto.SignInProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// API 인터페이스 정의
interface ApiService {
    @GET("/login")
    suspend fun login(@Query("loginId") loginId: String, @Query("password") password: String): ResponseObject

    @POST("/user")
    suspend fun signIn(@Body data: SignInProfile) : ResponseObject

    @GET("/user")
    suspend fun myPage(@Header("Authorization") Authorization: String): ResponseObject

    @PUT("/user")
    suspend fun editProfile(@Header("Authorization") Authorization: String,@Body data: EditProfile): ResponseObject



}