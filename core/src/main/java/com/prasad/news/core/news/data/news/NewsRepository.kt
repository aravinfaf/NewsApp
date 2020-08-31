package com.prasad.news.core.news.data.news
import com.prasad.news.core.data.Result
import com.prasad.news.core.news.data.news.model.NewsResponse

class NewsRepository(private val remoteDataSource: NewsRemoteDataSource) {

    suspend fun loadNews() =
        getData { remoteDataSource.loadNews() }

    private suspend fun getData(
        request: suspend () -> Result<NewsResponse>
    ): Result<NewsResponse> {
        return request()
    }

    companion object {
        @Volatile
        private var INSTANCE: NewsRepository? = null

        fun getInstance(remoteDataSource: NewsRemoteDataSource): NewsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NewsRepository(remoteDataSource).also {
                    INSTANCE = it
                }
            }
        }
    }
}