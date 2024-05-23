package com.android.myapplication.dto

data class ResponseObject(
    val message: String,
    val data: Any
)

data class DiscResultResponse(
    val message: String,
    val data: DiscResultData
)

data class DiscResultData(
    val discCode: DiscGetData
)

data class DiscGetData(
    val category: String,
    val pros: String,
    val ex: String,
    val prosJob: String,
    val job: String,
    val key: String
)

data class DiscUsers(
    val data: Int
)
