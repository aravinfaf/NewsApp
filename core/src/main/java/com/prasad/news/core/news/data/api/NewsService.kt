package com.prasad.news.core.news.data.api

import com.prasad.news.core.news.data.news.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("v2/everything")
    suspend fun getNewsData(@Query("q") q:String?,@Query("from") from:String?,@Query("sortBy") sortBy:String?,@Query("apiKey") apiKey: String?): Response<NewsResponse>

    companion object {
        const val ENDPOINT = "http://newsapi.org/"
    }

}