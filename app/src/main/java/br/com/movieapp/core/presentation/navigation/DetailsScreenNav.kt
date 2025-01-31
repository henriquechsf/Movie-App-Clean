package br.com.movieapp.core.presentation.navigation

import br.com.movieapp.core.util.Constants

sealed class DetailsScreenNav(val route: String) {

    object DetailScreen : DetailsScreenNav(
        route = "movie_detail_destination?${Constants.MOVIE_DETAIL_ARGUMENT_KEY}=" +
                "{${Constants.MOVIE_DETAIL_ARGUMENT_KEY}}"
    ) {
        fun passMovieId(movieId: Int) =
            "movie_detail_destination?${Constants.MOVIE_DETAIL_ARGUMENT_KEY}=$movieId"
    }
}