package com.prasad.news.core.news.domain

import com.prasad.news.core.news.data.news.NewsRepository
import javax.inject.Inject
import com.prasad.news.core.data.Result
import com.prasad.news.core.news.data.news.model.NewsResponse
import com.prasad.news.core.util.exhaustive

class NewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(): Result<NewsResponse> {
        val result = newsRepository.loadNews()
        when (result) {
            is Result.Success -> {
                val loadedData = result.data
                return Result.Success(loadedData)
            }
            is Result.Error -> {
                return result
            }
        }.exhaustive
    }
}