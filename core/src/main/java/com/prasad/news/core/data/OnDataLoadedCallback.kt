package com.prasad.news.core.data

interface OnDataLoadedCallback<T> {
    fun onDataLoaded(data: T)
}
