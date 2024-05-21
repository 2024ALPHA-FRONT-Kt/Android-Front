package com.android.myapplication.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    val apiservice: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://sunmi.site/") // 실제 엔드포인트 URL로 변경해야 합니다
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(ApiService::class.java)
    }
}