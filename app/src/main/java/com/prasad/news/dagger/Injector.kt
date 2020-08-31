@file:JvmName("Injector")
package com.prasad.news.dagger


import com.prasad.news.ui.HomeActivity
import com.prasad.news.ui.coreComponent


fun inject(activity: HomeActivity) {
    DaggerHomeComponent.builder()
        .coreComponent(activity.coreComponent())
        .homeActivity(activity)
        .build()
        .inject(activity)
}