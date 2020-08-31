package com.prasad.news.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import com.prasad.news.core.dagger.CoreComponent
import com.prasad.news.core.dagger.DaggerCoreComponent

class   NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.create()
    }

    companion object {
        @JvmStatic
        fun coreComponent(context: Context) =
            (context.applicationContext as NewsApplication).coreComponent
    }
}

fun Activity.coreComponent() = NewsApplication.coreComponent(this)
