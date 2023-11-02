package com.dullgames.moviesinfohub.data.models

data class Movies(
    val page: Int = 0,
    val results: List<Movie> = emptyList(),
    val total_pages: Int = 0,
    val total_results: Int = 0
)