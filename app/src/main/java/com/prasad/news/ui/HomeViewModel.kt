package com.prasad.news.ui

import androidx.lifecycle.*
import com.prasad.news.core.data.CoroutinesDispatcherProvider
import com.prasad.news.core.data.DataLoadingSubject
import com.prasad.news.core.data.DataManager
import com.prasad.news.core.data.OnDataLoadedCallback
import com.prasad.news.core.news.data.news.model.NewsResponse
import com.prasad.news.core.ui.model.NewsProgressUiModel
import com.prasad.news.core.ui.model.NewsUiModel
import kotlinx.coroutines.launch

class HomeViewModel (
    private val dataManager: DataManager,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {
    private val _newsProgress = MutableLiveData<NewsProgressUiModel>()
    val newsProgress: LiveData<NewsProgressUiModel>
        get() = _newsProgress

    private val newsData = MutableLiveData<NewsResponse>()

    private val onDataLoadedCallback = object : OnDataLoadedCallback<NewsResponse> {
        override fun onDataLoaded(data: NewsResponse) {
            updateApiData(data)
        }
    }

    private fun updateApiData(data: NewsResponse) {
        newsData.postValue(data)
    }

    private val dataLoadingCallbacks = object : DataLoadingSubject.DataLoadingCallbacks {
        override fun dataStartedLoading() {
            _newsProgress.postValue(NewsProgressUiModel(true))
        }

        override fun dataFinishedLoading() {
            _newsProgress.postValue(NewsProgressUiModel(false))
        }
    }

    init {
        dataManager.setOnDataLoadedCallback(onDataLoadedCallback)
        dataManager.registerCallback(dataLoadingCallbacks)
        loadData(true)
    }



    private fun loadData(isLoad: Boolean) = viewModelScope.launch {
        dataManager.loadMore(isLoad)
    }

    fun getNewsItem() = newsData.switchMap {
        liveData(viewModelScope.coroutineContext + dispatcherProvider.computation) {
            emit(NewsUiModel(it))
        }
    }

    override fun onCleared() {
        dataManager.cancelLoading()
        super.onCleared()
    }
}