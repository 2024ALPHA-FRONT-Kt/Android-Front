package com.android.myapplication.api

import com.android.myapplication.dto.EditProfile
import com.android.myapplication.dto.ResponseObject
import com.android.myapplication.dto.SignInProfile
import com.android.myapplication.ui.disc.data_class.DiscTestResult
import com.android.myapplication.ui.free_community.data_class.EditingFree
import com.android.myapplication.ui.free_community.data_class.PostingFree
import com.android.myapplication.ui.knowledge_community.data_class.EditingKnowledge
import com.android.myapplication.ui.knowledge_community.data_class.PostingKComment
import com.android.myapplication.ui.knowledge_community.data_class.PostingKnowledge
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
    suspend fun login(
        @Query("loginId") loginId: String,
        @Query("password") password: String
    ): ResponseObject

    @POST("/user")
    suspend fun signIn(@Body data: SignInProfile): ResponseObject

    @GET("/user")
    suspend fun myPage(@Header("Authorization") Authorization: String): ResponseObject

    @PUT("/user")
    suspend fun editProfile(
        @Header("Authorization") Authorization: String,
        @Body data: EditProfile
    ): ResponseObject

    @GET("/posts")
    suspend fun knowLedgeLists(
        @Header("Authorization") authorization: String,
        @Query("postType") postType: String,
        @Query("page") page: Int
    ): ResponseObject

    @POST("/post")
    suspend fun postingKnowledgePost(
        @Header("Authorization") authorization: String,
        @Body knowledgePost: PostingKnowledge
    ): ResponseObject

    @DELETE("/post")
    suspend fun deleteKnowledge(
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    ): ResponseObject

    @PATCH("/post")
    suspend fun editKnowledge(
        @Header("Authorization") authorization: String,
        @Body editKnowledge: EditingKnowledge
    ): ResponseObject

    @GET("/post")
    suspend fun knowledgePostDetail(
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    ): ResponseObject

    @POST("/DISC")
    suspend fun postDiscTestResult(
        @Header("Authorization") token: String,
        @Body discTestResult: DiscTestResult
    ): ResponseObject

    @GET("/DISC")
    suspend fun getDiscTestResult(
        @Header("Authorization") token: String
    ): ResponseObject


    @GET("/DISC-headcount")
    suspend fun getDiscUsers(
        @Header("Authorization") token: String
    ): ResponseObject

    @GET("/posts")
    suspend fun freeLists(
        @Header("Authorization") authorization: String,
        @Query("postType") postType: String,
        @Query("page") page: Int
    ): ResponseObject

    @POST("/comment")
    suspend fun postingKComment(
        @Header("Authorization") token: String,
        @Body postKComment: PostingKComment
    ): ResponseObject

    @POST("/post")
    suspend fun postingFreePost(
        @Header("Authorization") authorization: String,
        @Body freePost: PostingFree
    ): ResponseObject

    @DELETE("/post")
    suspend fun deleteFree(
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    ): ResponseObject

    @PATCH("/post")
    suspend fun editFree(
        @Header("Authorization") authorization: String,
        @Body editFree: EditingFree
    ): ResponseObject

    @GET("/post")
    suspend fun freePostDetail(
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    ): ResponseObject

    @GET("/hot-post")
    suspend fun loadHotFreePost(
        @Header("Authorization") authorization: String,
        @Query("postType") postType: String
    ): ResponseObject

    @PATCH("/user/report")
    suspend fun report(
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    ): ResponseObject
}