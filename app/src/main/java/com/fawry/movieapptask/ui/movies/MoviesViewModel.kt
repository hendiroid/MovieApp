package com.fawry.movieapptask.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fawry.movieapptask.business.models.Item
import com.fawry.movieapptask.business.models.MovieDataModel
import com.fawry.movieapptask.business.network.ApiRequestManagerInterface
import com.fawry.movieapptask.business.network.InternetConnectionManagerInterface
import com.fawry.movieapptask.business.network.MoviesRepoInterface
import com.fawry.movieapptask.business.network.NetworkErrors.ERROR_CHECK_NETWORK_CONNECTION
import com.fawry.movieapptask.business.models.Message
import com.fawry.movieapptask.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class MoviesViewModel
@Inject
constructor(
    private val apiRequestManager: ApiRequestManagerInterface,
    private val repo: MoviesRepoInterface,
    private val internetConnectionManager: InternetConnectionManagerInterface
) : ViewModel() {

    val moviesViewState = MutableLiveData<ViewState<List<Item>>>()

    fun getMovies() {
        if (internetConnectionManager.isConnectedToInternet) {
            moviesViewState.value = ViewState.Loading
            apiRequestManager.execute(
                request = {
                    repo.getMovies("movie", "day", API_KEY)
                },
                onSuccess = { data, _ ->
                    moviesViewState.value = ViewState.Success(data.results)

                },
                onFailure = {
                    moviesViewState.value = ViewState.Error(it)
                }
            )
        } else {
            moviesViewState.value =
                ViewState.Error(Message(message = ERROR_CHECK_NETWORK_CONNECTION))
        }
    }

    val movieDetailsViewState = MutableLiveData<ViewState<MovieDataModel>>()

    fun getMovieDetails(id: Int) {
        if (internetConnectionManager.isConnectedToInternet) {
            movieDetailsViewState.value = ViewState.Loading
            apiRequestManager.execute(
                request = {
                    repo.getMovieDetails(id, API_KEY)
                },
                onSuccess = { data, _ ->
                    movieDetailsViewState.value = ViewState.Success(data)

                },
                onFailure = {
                    movieDetailsViewState.value = ViewState.Error(it)
                }
            )
        } else {
            movieDetailsViewState.value =
                ViewState.Error(Message(message = ERROR_CHECK_NETWORK_CONNECTION))
        }
    }
}

const val API_KEY = "c50f5aa4e7c95a2a553d29b81aad6dd0"

