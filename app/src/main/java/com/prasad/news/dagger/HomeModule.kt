package com.prasad.news.dagger

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.prasad.news.core.dagger.DataManagerModule
import com.prasad.news.core.ui.ConnectivityChecker
import com.prasad.news.ui.HomeActivity
import com.prasad.news.ui.HomeViewModel
import com.prasad.news.ui.HomeViewModelFactory

import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [DataManagerModule::class])
abstract class HomeModule {
    @Binds
    abstract fun homeActivityAsFragmentActivity(activity: HomeActivity): FragmentActivity

    @Binds
    abstract fun homeActivityAsActivity(activity: HomeActivity): Activity

    @Binds
    abstract fun context(activity: Activity): Context

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun homeViewModel(
            factory: HomeViewModelFactory,
            fragmentActivity: FragmentActivity
        ): HomeViewModel {
            return ViewModelProvider(fragmentActivity, factory).get(HomeViewModel::class.java)
        }

        @JvmStatic
        @Provides
        fun connectivityChecker(activity: Activity): ConnectivityChecker? {
            val connectivityManager = activity.getSystemService<ConnectivityManager>()
            return if (connectivityManager != null) {
                ConnectivityChecker(connectivityManager)
            } else {
                null
            }
        }
    }
}