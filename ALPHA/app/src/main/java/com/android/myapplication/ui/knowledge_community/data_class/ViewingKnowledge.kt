package com.android.myapplication.ui.knowledge_community.data_class

data class ViewingKnowledge(
    val postId: String,
    val email: String,
    val univ: String,
    val title: String,
    val content: String,
    val views: Int,
    val responseCommentDto: List<ResponseCommentDto>
)

data class ResponseCommentDto(
    val userId: String,
    val univ: String,
    val email: String,
    val content: String
)