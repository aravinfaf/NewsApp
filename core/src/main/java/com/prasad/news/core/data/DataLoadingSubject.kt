package com.prasad.news.core.data

interface DataLoadingSubject {
    fun registerCallback(callbacks: DataLoadingCallbacks)

    interface DataLoadingCallbacks {
        fun dataStartedLoading()
        fun dataFinishedLoading()
    }
}