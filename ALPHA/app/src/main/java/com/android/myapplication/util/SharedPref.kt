package com.android.myapplication.util

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)
    fun addToken(tokenName: String, token:String) {
        val editor = pref.edit()
        editor.putString(tokenName,token)
        editor.apply()
    }
    fun getToken(key: String, defValue: String): String {
        return pref.getString(key, defValue).toString()
    }

}