package com.android.myapplication.ui.free_community.data_class

data class ViewingFree(
    val postId: String,
    val userId: String,
    val univ: String,
    val email: String,
    val title: String,
    val content: String,
    val like: Boolean,
    val likeNumber: Int,
    val views: Int,
    val commentNumber: Int,
    val responseCommentDto: List<ResponseCommentDto>
)

data class ResponseCommentDto(
    val userId: String,
    val univ: String,
    val email: String,
    val content: String
)