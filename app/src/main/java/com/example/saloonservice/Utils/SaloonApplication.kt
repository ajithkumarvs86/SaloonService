package com.example.saloonservice.Utils

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

/**
* Purpose:
 * to pass Global context
* */

class SaloonApplication:Application() {
    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
    }
}