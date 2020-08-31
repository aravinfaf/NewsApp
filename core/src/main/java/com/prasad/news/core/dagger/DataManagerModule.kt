package com.prasad.news.core.dagger


import com.prasad.news.core.dagger.news.NewsDataModule
import com.prasad.news.core.dagger.scope.FeatureScope
import com.prasad.news.core.data.CoroutinesDispatcherProvider
import com.prasad.news.core.data.DataManager
import com.prasad.news.core.news.domain.NewsUseCase
import dagger.Module
import dagger.Provides

@Module(includes = [NewsDataModule::class])
class DataManagerModule {
    @Provides
    @FeatureScope
    fun provideDataManager(
        loadNews: NewsUseCase,
        coroutinesDispatcherProvider: CoroutinesDispatcherProvider
    ): DataManager = getDataManager(
        loadNews,
        coroutinesDispatcherProvider
    )

    private fun getDataManager(
        loadNews: NewsUseCase,
        coroutinesDispatcherProvider: CoroutinesDispatcherProvider
    ): DataManager {
        return DataManager(
            loadNews,
            coroutinesDispatcherProvider
        )
    }
}