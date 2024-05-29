package com.android.myapplication.api

import com.android.myapplication.dto.UnivCert.Certify
import com.android.myapplication.dto.UnivCert.CertifyCode
import com.android.myapplication.dto.UnivCert.Check
import com.android.myapplication.dto.UnivCert.Clear
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UnivCertService {

    // 인증 가능한 대학교 명인지 체킹
    @POST("/api/v1/check")
    suspend fun check ( @Body data : Check )

    // 대학명 이메일 서버에 보내서 인증 시작
    @POST("/api/v1/certify")
    suspend fun certify ( @Body data : Certify )

    // 발송된 인증코드 입력해서 서버에 보내기
    @POST("/api/v1/certifycode")
    suspend fun certifyCode ( @Body data : CertifyCode )

    @POST("/api/v1/clear")
    suspend fun clear ( @Body data: Clear)
}