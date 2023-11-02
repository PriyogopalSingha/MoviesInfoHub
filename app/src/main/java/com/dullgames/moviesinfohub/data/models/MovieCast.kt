package com.dullgames.moviesinfohub.data.models

data class MovieCast(
    val cast: List<Cast> = emptyList(),
    val id: Int = 0
)