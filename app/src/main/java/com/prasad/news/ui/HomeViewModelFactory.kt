package com.prasad.news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prasad.news.core.data.CoroutinesDispatcherProvider
import com.prasad.news.core.data.DataManager

import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val dataManager: DataManager,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != HomeViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return HomeViewModel(
            dataManager,
            dispatcherProvider
        ) as T
    }
}