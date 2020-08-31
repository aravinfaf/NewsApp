package com.prasad.news.core.ui.model

import com.prasad.news.core.news.data.news.model.NewsResponse

data class NewsUiModel(
    val items: NewsResponse
)

data class NewsProgressUiModel(
    val isLoading: Boolean
)
