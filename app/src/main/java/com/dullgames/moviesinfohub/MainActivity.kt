package com.dullgames.moviesinfohub

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dullgames.moviesinfohub.data.models.Movie
import com.dullgames.moviesinfohub.ui.MoviesViewModel
import com.dullgames.moviesinfohub.ui.theme.MoviesInfoHubTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesInfoHubTheme {
                val textState = remember { mutableStateOf(TextFieldValue("")) }
                val isSearchVisible = remember { mutableStateOf(false) }
                MainScreen(navController = rememberNavController(), textState = textState, isSearchVisible = isSearchVisible)
            }
        }
    }
}

@Composable
fun MainScreen(
    navController: NavController,
    textState: MutableState<TextFieldValue>,
    isSearchVisible: MutableState<Boolean>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AddTopBar(
            navController = navController,
            textState = textState,
            isSearchVisible = isSearchVisible
        )
        ShowMovies()
    }
}

@Composable
fun SearchBarScreen(
    navController: NavController,
    textState: MutableState<TextFieldValue>,
    isSearchVisible: MutableState<Boolean>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            OutlinedTextField(value = textState.value, onValueChange = {
                textState.value = it
            }, trailingIcon = {
                if (textState.value != TextFieldValue("")) {
                    IconButton(onClick = { textState.value = TextFieldValue("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            }, leadingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier.clickable {
                        isSearchVisible.value = !isSearchVisible.value
                        textState.value = TextFieldValue("")
                    })
            }, modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
            )

        }
        SearchItemList(navController = navController, text = textState)

    }

}

@Composable
fun AddTopBar(
    navController: NavController,
    textState: MutableState<TextFieldValue>,
    isSearchVisible: MutableState<Boolean>
) {

    if (isSearchVisible.value) {
        SearchBarScreen(
            navController = navController,
            textState = textState,
            isSearchVisible = isSearchVisible
        )
    } else {
        MenuBarMainScreen(isSearchVisible)
    }


}

@Composable
fun MenuBarMainScreen(isSearchVisible: MutableState<Boolean>) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = "MOVIES APP",
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            fontSize = 26.sp
        )
        IconButton(onClick = { isSearchVisible.value = !isSearchVisible.value }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(30.dp)
            )
        }
    }

}

@Composable
fun SearchItemList(navController: NavController, text: MutableState<TextFieldValue>) {
    val searchedText = text.value.text
    val viewModel = hiltViewModel<MoviesViewModel>()
    viewModel.getSearchMoviesList(searchedText)
    val searchResults = viewModel.moviesList.collectAsState()
    val moviesList = searchResults.value.results
    var filteredList: List<Movie>
    LazyColumn {
        filteredList = if (searchedText.isEmpty()) {
            emptyList()
        } else {
            val resultList = ArrayList<Movie>()
            for (movie in moviesList) {
                if (movie.original_title.lowercase(Locale.getDefault())
                        .contains(searchedText.lowercase(Locale.getDefault()))
                ) {
                    resultList.add(movie)
                }
            }
            resultList
        }
        items(filteredList) { item ->
            SearchItem(movie = item)
        }
    }
}

@Composable
fun SearchItem(movie: Movie) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        val painter = if(movie.poster_path == null) painterResource(id = R.drawable.no_image)
            else rememberAsyncImagePainter(model = "https://image.tmdb.org/t/p/w500${movie.poster_path}")
        Image(
            painter = painter, contentDescription = "null", modifier = Modifier
                .width(80.dp)
                .height(85.dp)
                .padding(8.dp),
            contentScale = ContentScale.FillBounds
        )
        Text(text = movie.original_title, maxLines = 1)
    }
}

@Composable
fun ShowMovies() {
    val viewModel = hiltViewModel<MoviesViewModel>()
    val popular_state by viewModel.popularMoviesList.collectAsState()
    val trending_movies_state by viewModel.trendingMoviesList.collectAsState()
    val top_rated_movies_state by viewModel.topRatedMoviesList.collectAsState()
    val trending_tv_state by viewModel.trendingTVList.collectAsState()

    LazyColumn {
        item {
            Text(
                text = "Popular Movies",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(4.dp)
            )

            ScrollableList(movies = popular_state.results, landscape = true)
            Spacer(modifier = Modifier.padding(6.dp))
        }

        item {
            Text(
                text = "Trending Movies",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(4.dp)
            )
            ScrollableList(movies = trending_movies_state.results)
            Spacer(modifier = Modifier.padding(6.dp))
        }
        item {
            Text(
                text = "Top Rated Movies",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(4.dp)
            )
            ScrollableList(movies = top_rated_movies_state.results)
            Spacer(modifier = Modifier.padding(6.dp))
        }
    }
}

@Composable
fun ScrollableList(movies: List<Movie>, landscape: Boolean = false) {
    LazyRow(modifier = Modifier.padding(4.dp)) {
        items(items = movies) { movie ->
            MovieItem(movie = movie, landscape)
            Spacer(modifier = Modifier.padding(6.dp))
        }
    }
}

@Composable
fun MovieItem(movie: Movie, landscape: Boolean = false) {
    Column(
        modifier = Modifier
            .width(if (landscape) 280.dp else 140.dp)
            .height(if (landscape) 280.dp else 170.dp)
            .clickable { }

    )
    {
        val painter =
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500${movie.poster_path}").crossfade(500)
                    .build(),
                placeholder = painterResource(
                    id = R.drawable.placeholder_img
                ),
                contentScale = ContentScale.FillBounds
            )
        Image(
            painter = painter, contentDescription = "null", modifier = Modifier
                .fillMaxWidth()
                .height(if (landscape) 260.dp else 150.dp)
                .clip(RoundedCornerShape(size = 14.dp)),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = movie.original_title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1
        )

    }
}
