package com.android.myapplication.dto

data class SignInProfile(
    val userRole: String,
    val name: String,
    val email: String,
    val gender: String,
    val age: Int,
    val loginId: String,
    val password: String,
    val phoneNum: String?,
    val univ: String,
    val department: String,
    val image: String?,
)
