package com.android.myapplication.api

import com.android.myapplication.dto.EditProfile
import com.android.myapplication.dto.ResponseObject
import com.android.myapplication.ui.free_community.data_class.EditingFree
import com.android.myapplication.ui.free_community.data_class.PostingFree
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

// API 인터페이스 정의
interface ApiService {
    @GET("/login")
    suspend fun login(@Query("loginId") loginId: String, @Query("password") password: String): ResponseObject

    @GET("/user")
    suspend fun myPage(@Header("Authorization") Authorization: String): ResponseObject

    @PUT("/user")
    suspend fun editProfile(@Header("Authorization") Authorization: String,@Body data: EditProfile): ResponseObject

    @GET("/posts")
    suspend fun freeLists(
        @Header("Authorization") authorization: String,
        @Query("postType") postType: String,
        @Query("page") page: Int
    ) : ResponseObject

    @POST("/post")
    suspend fun postingKnowledgePost(
        @Header("Authorization") authorization: String,
        @Body freePost: PostingFree
    ) : ResponseObject

    @DELETE("/post")
    suspend fun deleteFree(
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    ) : ResponseObject

    @PATCH("/post")
    suspend fun editFree(
        @Header("Authorization") authorization: String,
        @Body editFree: EditingFree
    ) : ResponseObject

    @GET("/post")
    suspend fun freePostDetail(
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    ) : ResponseObject

    @GET("/hot-post")
    suspend fun loadHotFreePost(
        @Header("Authorization") authorization: String,
        @Query("postType") postType: String
    ) : ResponseObject
}