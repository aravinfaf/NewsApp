package com.prasad.news.core.dagger.news

import com.google.gson.Gson
import com.prasad.news.core.dagger.scope.FeatureScope
import com.prasad.news.core.news.data.api.NewsService
import com.prasad.news.core.news.data.news.NewsRemoteDataSource
import com.prasad.news.core.news.data.news.NewsRepository
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NewsDataModule {

    @Provides
    @FeatureScope
    fun provideNewsService(
        client: Lazy<OkHttpClient>,
        gson: Gson
    ): NewsService {
        return Retrofit.Builder()
            .baseUrl(NewsService.ENDPOINT)
            .callFactory(client.get())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NewsService::class.java)
    }

    @Provides
    @FeatureScope
    fun provideNewsRepository(
        newsRemoteDataSource: NewsRemoteDataSource
    ): NewsRepository =
        NewsRepository.getInstance(newsRemoteDataSource)

    @Provides
    @FeatureScope
    fun provideNewsRemoteDataSource(service: NewsService): NewsRemoteDataSource {
        return NewsRemoteDataSource.getInstance(service)
    }
}