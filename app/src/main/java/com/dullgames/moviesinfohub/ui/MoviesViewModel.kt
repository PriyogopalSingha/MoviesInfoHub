package com.dullgames.moviesinfohub.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dullgames.moviesinfohub.ViewState
import com.dullgames.moviesinfohub.data.network.MovieRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val movieRepoImpl: MovieRepoImpl): ViewModel() {


    init {
        viewModelScope.launch {
            movieRepoImpl.getPopularMovies()
        }
        viewModelScope.launch {
            movieRepoImpl.getTrendingMovies()
        }
        viewModelScope.launch {
            movieRepoImpl.getTrendingTVSeries()
        }
        viewModelScope.launch {
            movieRepoImpl.getTopRatedMovies()
        }
    }
    val moviesList = movieRepoImpl.moviesListFlow
    val popularMoviesList = movieRepoImpl.popularMoviesListFlow
    val trendingMoviesList = movieRepoImpl.trendingMoviesListFlow
    val topRatedMoviesList = movieRepoImpl.topRatedMoviesListFlow
    val trendingTVList = movieRepoImpl.tvseriesListFlow
    val movieDetails = movieRepoImpl.movieDetailsFlow
    val movieCast = movieRepoImpl.movieCastFlow


    fun getSearchMoviesList(query: String){
        viewModelScope.launch {
            movieRepoImpl.getMoviesList(query)
        }
    }

    fun getMovieDetails(id: Int?){
        viewModelScope.launch {
            movieRepoImpl.getMovieDetails(id)
        }
    }

    fun getMovieCast(id: Int?){
        viewModelScope.launch {
            movieRepoImpl.getMovieCast(id)
        }
    }
}