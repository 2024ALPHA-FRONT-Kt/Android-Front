package com.android.myapplication.api

import retrofit2.http.POST
import retrofit2.http.Query

interface UnivCertService {

    // 인증 가능한 대학교 명인지 체킹
    @POST("/api/v1/check")
    suspend fun check(
        @Query("univName") univName : String,
    )

    // 대학명 이메일 서버에 보내서 인증 시작
    @POST("/api/v1/certify")
    suspend fun certify(
        @Query("key") key : String,
        @Query("email") email : String,
        @Query("univName") univName : String,
        @Query("univ_check") univCheck : Boolean,
        )

    // 발송된 인증코드 입력해서 서버에 보내기
    @POST("/api/v1/certifycode")
    suspend fun certifyCode(
        @Query("key") key : String,
        @Query("email") email : String,
        @Query("univName") univName : String,
        @Query("code") code : Int
    )
}