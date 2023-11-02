package com.dullgames.moviesinfohub.data.models

data class TVSeriesResult(
    val page: Int=0,
    val results: List<TVSeries> = emptyList(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)