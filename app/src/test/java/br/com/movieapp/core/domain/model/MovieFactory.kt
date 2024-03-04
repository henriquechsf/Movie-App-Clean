package br.com.movieapp.core.domain.model

class MovieFactory {

    fun create(poster: Poster) = when (poster) {
        Poster.Avengers -> {
            Movie(id = 1, title = "Avergers", voteAverage = 7.1, imageUrl = "Url")
        }

        Poster.SpiderMan -> {
            Movie(id = 2, title = "SpiderMan", voteAverage = 7.9, imageUrl = "Url")
        }
    }

    sealed class Poster {
        object Avengers : Poster()
        object SpiderMan : Poster()
    }
}