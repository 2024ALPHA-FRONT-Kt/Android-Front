package com.android.myapplication

import android.app.Application
import com.android.myapplication.util.SharedPref

class App : Application() {
    companion object {
        lateinit var prefs: SharedPref
    }

    override fun onCreate() {
        prefs = SharedPref(applicationContext)
        super.onCreate()
    }
}