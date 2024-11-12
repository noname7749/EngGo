package com.example.enggo

import android.app.Application
import com.example.enggo.data.AppContainer
import com.example.enggo.data.DefaultAppContainer

class EngGoApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
