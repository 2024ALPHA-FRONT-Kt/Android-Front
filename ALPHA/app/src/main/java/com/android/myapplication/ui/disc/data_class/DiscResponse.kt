package com.android.myapplication.ui.disc.data_class

data class DiscTestResponse(
    val message: String,
    val data: DiscTestData
)

data class DiscTestData(
    val sameDISCNum: Int,
    val discCode: String
)
