package com.prasad.news.core.news.data.news

import com.prasad.news.core.BuildConfig
import com.prasad.news.core.news.data.api.NewsService
import com.prasad.news.core.news.data.news.model.NewsResponse
import retrofit2.Response
import java.io.IOException
import com.prasad.news.core.data.Result

class NewsRemoteDataSource(private val service: NewsService) {

    suspend fun loadNews(): Result<NewsResponse> {
        return try {
            val response = service.getNewsData(
                q = "bitcoin",
                from = "2020-06-02",
                sortBy = "publishedAt",
                apiKey = BuildConfig.NEWS_APIKEY
            )

            getResult(response = response, onError = {
                Result.Error(
                    IOException("Error getting stories ${response.code()} ${response.message()}")
                )
            })
        } catch (e: Exception) {
            Result.Error(IOException("Error getting stories", e))
        }
    }

    private inline fun getResult(
        response: Response<NewsResponse>,
        onError: () -> Result.Error
    ): Result<NewsResponse> {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.Success(body)
            }
        }
        return onError.invoke()
    }

    companion object {
        @Volatile
        private var INSTANCE: NewsRemoteDataSource? = null

        fun getInstance(service: NewsService): NewsRemoteDataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NewsRemoteDataSource(service).also { INSTANCE = it }
            }
        }
    }
}