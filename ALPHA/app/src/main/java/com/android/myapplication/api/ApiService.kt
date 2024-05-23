package com.android.myapplication.api

import com.android.myapplication.dto.EditProfile
import com.android.myapplication.dto.ResponseObject
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
    suspend fun signIn(
        @Query("userRole") userRole: String,
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("gender") gender: String,
        @Query("age") age: Int,
        @Query("loginId") loginId: String,
        @Query("password") password: String,
        @Query("phoneNum") phoneNum: String?,
        @Query("univ") univ: String,
        @Query("department") department: String,
        @Query("image") image: String?,
    )

    @GET("/user")
    suspend fun myPage(@Header("Authorization") Authorization: String): ResponseObject

    @PUT("/user")
    suspend fun editProfile(@Header("Authorization") Authorization: String,@Body data: EditProfile): ResponseObject



}