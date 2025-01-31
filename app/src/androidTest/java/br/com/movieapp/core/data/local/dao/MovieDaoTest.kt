package br.com.movieapp.core.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import br.com.movieapp.core.data.local.MovieDatabase
import br.com.movieapp.core.data.local.entity.MovieEntity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class MovieDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: MovieDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setup() {
        hiltRule.inject()
        movieDao = database.movieDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_getMovies_should_return_list_of_movies() = runTest {
        //Given - Nothing

        //When
        val movies = movieDao.getMovies().first()

        //Then
        assertThat(movies.size).isEqualTo(0)
    }

    @Test
    fun test_getMovies_should_return_movies_ordered_by_id() = runTest {
        //Given
        val movieEntities = listOf(
            MovieEntity(movieId = 1, title = "Filme 1", imageUrl = "url1"),
            MovieEntity(movieId = 4, title = "Filme 2", imageUrl = "url2"),
            MovieEntity(movieId = 2, title = "Filme 3", imageUrl = "url3"),
            MovieEntity(movieId = 3, title = "Filme 4", imageUrl = "url4"),
        )
        movieDao.insertMovies(movieEntities)

        //When
        val movies = movieDao.getMovies().first()

        //Then
        assertThat(movies.size).isEqualTo(4)
        assertThat(movies[0].movieId).isEqualTo(1)
        assertThat(movies[1].movieId).isEqualTo(2)
        assertThat(movies[2].movieId).isEqualTo(3)
        assertThat(movies[3].movieId).isEqualTo(4)
    }

    @Test
    fun test_getMovie_should_return_correct_movie_by_id() = runTest {
        //Given
        val movieEntity = MovieEntity(movieId = 1, title = "Filme 1", imageUrl = "url1")
        movieDao.insertMovie(movieEntity)
        val movies = movieDao.getMovies().first()
        val movieClick = movies[0]

        //When
        val movieId = movieDao.getMovie(movieClick.movieId)

        //Then
        assertThat(movieId?.title).isEqualTo(movieClick.title)
    }

    @Test
    fun test_insertMovies_should_insert_movies_successfully() = runTest {
        //Given
        val movieEntities = listOf(
            MovieEntity(movieId = 1, title = "Filme 1", imageUrl = "url1"),
            MovieEntity(movieId = 4, title = "Filme 2", imageUrl = "url2"),
            MovieEntity(movieId = 2, title = "Filme 3", imageUrl = "url3"),
            MovieEntity(movieId = 3, title = "Filme 4", imageUrl = "url4"),
        )

        //When
        movieDao.insertMovies(movieEntities)

        //Then
        val insertedMovies = movieDao.getMovies().first()
        assertThat(movieEntities.size).isEqualTo(insertedMovies.size)
        assertThat(insertedMovies.containsAll(movieEntities))
    }

    @Test
    fun test_insertMovie_should_insert_a_movie_successfully() = runTest {
        //Given
        val movieEntity = MovieEntity(movieId = 1, title = "Filme 1", imageUrl = "url1")

        //When
        movieDao.insertMovie(movieEntity)

        //Then
        val movies = movieDao.getMovies().first()
        assertThat(movies[0].title).isEqualTo(movieEntity.title)
    }

    @Test
    fun test_isFavorite_should_return_favorite_movie_when_movie_is_marked_as_favorite() = runTest {
        //Given
        val movieId = 5321
        val favoriteMovie = MovieEntity(movieId = movieId, title = "Filme 1", imageUrl = "url")
        movieDao.insertMovie(favoriteMovie)

        //When
        val result = movieDao.isFavorite(movieId)

        //Then
        assertThat(result).isEqualTo(favoriteMovie)
    }

    @Test
    fun test_isFavorite_should_return_null_when_movie_is_not_marked_as_favorite() = runTest {
        //Given
        val movieId = 5321

        //When
        val result = movieDao.isFavorite(movieId)

        //Then
        assertThat(result).isNull()
    }

    @Test
    fun test_updateMovie_should_update_a_movie_successfully() = runTest {
        //Given
        val movieEntity = MovieEntity(movieId = 1, title = "Filme 1", imageUrl = "url1")
        movieDao.insertMovie(movieEntity)
        val allMovies = movieDao.getMovies().first()
        val updateMovie = allMovies[0].copy(title = "Filme atualizado")

        //When
        movieDao.insertMovie(updateMovie)

        //Then
        val movies = movieDao.getMovies().first()
        assertThat(movies[0].title).contains(updateMovie.title)
    }

    @Test
    fun test_deleteMovie_should_delete_a_movie_successfully() = runTest {
        //Given
        val movieEntity = MovieEntity(movieId = 1, title = "Filme 1", imageUrl = "url1")
        movieDao.insertMovie(movieEntity)

        //When
        movieDao.deleteMovie(movieEntity)

        //Then
        val movies = movieDao.getMovies().first()
        assertThat(movies).isEmpty()
    }
}