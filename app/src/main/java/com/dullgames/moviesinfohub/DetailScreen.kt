package com.dullgames.moviesinfohub

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dullgames.moviesinfohub.ui.MoviesViewModel

@Composable
fun DetailScreen(id: Int?) {
    val viewModel = hiltViewModel<MoviesViewModel>()
    viewModel.getMovieDetails(id)
    viewModel.getMovieCast(id)
    val movieDetails by viewModel.movieDetails.collectAsState()
    val movieCast by viewModel.movieCast.collectAsState()
    Column {
        val painter =
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500${movieDetails.poster_path}")
                    .build(),
                placeholder = painterResource(
                    id = R.drawable.placeholder_img
                ),
                contentScale = ContentScale.FillBounds
            )
        Image(
            painter = painter, contentDescription = "null", modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(size = 14.dp)),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = movieDetails.original_title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1
        )

    }
}