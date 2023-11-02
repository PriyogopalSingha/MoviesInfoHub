package com.dullgames.moviesinfohub

import com.dullgames.moviesinfohub.data.models.Movies
import com.dullgames.moviesinfohub.data.models.TVSeriesResult

sealed class ViewState{
    object Loading: ViewState()
    data class Success(val movies: Movies? = null, val series: TVSeriesResult? = null): ViewState()
    data class Error(val msg: String): ViewState()
}
