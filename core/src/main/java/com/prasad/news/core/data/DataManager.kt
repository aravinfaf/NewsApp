package com.prasad.news.core.data

import com.prasad.news.core.news.data.news.model.NewsResponse
import com.prasad.news.core.news.domain.NewsUseCase
import com.prasad.news.core.util.exhaustive
import kotlinx.coroutines.*
import javax.inject.Inject

private data class InFlightRequestData(val load: Boolean)

class DataManager @Inject constructor(
    private val loadNews: NewsUseCase,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : DataLoadingSubject {
    private var parentJob = SupervisorJob()
    private val scope = CoroutineScope(dispatcherProvider.computation + parentJob)
    private val parentJobs = mutableMapOf<InFlightRequestData,Job>()
    private var loadingCallbacks = mutableListOf<DataLoadingSubject.DataLoadingCallbacks>()
    private var onDataLoadedCallback: OnDataLoadedCallback<NewsResponse>? = null

    fun setOnDataLoadedCallback(
        onDataLoadedCallback: OnDataLoadedCallback<NewsResponse>?
    ) {
        this.onDataLoadedCallback = onDataLoadedCallback
    }

    private fun onDataLoaded(data: NewsResponse) {
        onDataLoadedCallback?.onDataLoaded(data)
    }

    suspend fun loadMore(load: Boolean) =
        withContext(dispatcherProvider.computation) {
            loadData(load)
        }

    private fun loadData(load: Boolean) {
        loadStarted()
        val data = InFlightRequestData(load)
        parentJobs[data] = launchLoadNews(data)
    }

    private fun launchLoadNews(data: InFlightRequestData) = scope.launch {
        val result = loadNews()
        when (result) {
            is Result.Success -> sourceLoaded(result.data,data)
            is Result.Error -> loadFailed(data)
        }.exhaustive
    }

    private fun sourceLoaded(
        data: NewsResponse,
        request: InFlightRequestData
    ) {
        loadFinished()
        onDataLoaded(data)
        parentJobs.remove(request)
    }

    private fun loadFailed(request: InFlightRequestData) {
        loadFinished()
        parentJobs.remove(request)
    }

    override fun registerCallback(callbacks: DataLoadingSubject.DataLoadingCallbacks) {
        loadingCallbacks.add(callbacks)
    }

    fun cancelLoading() {
        parentJobs.values.forEach { it.cancel() }
        parentJobs.clear()
    }

    private fun loadStarted() {
        dispatchLoadingStartedCallbacks()
    }

    private fun loadFinished() {
        dispatchLoadingFinishedCallbacks()
    }

    private fun dispatchLoadingStartedCallbacks() {
        loadingCallbacks.forEach {
            it.dataStartedLoading()
        }
    }

    private fun dispatchLoadingFinishedCallbacks() {
        loadingCallbacks.forEach {
            it.dataFinishedLoading()
        }
    }
}