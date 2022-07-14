package com.fawry.movieapptask.business.network

import com.fawry.movieapptask.business.models.MovieDataModel
import com.fawry.movieapptask.business.models.MovieListModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface MoviesApi {

    @GET("${Constants.Endpoints.trending}/{media_type}/{time_window}")
    suspend fun getMovies(
        @Path("media_type") media_type: String,
        @Path("time_window") time_window: String,
        @Query("api_key") api_key: String
    ): Response<MovieListModel>

    @GET("${Constants.Endpoints.movie}/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") api_key: String
    ): Response<MovieDataModel>
}

interface MoviesRepoInterface {
    suspend fun getMovies(
        media_type: String,
        time_window: String,
        api_key: String
    ): Response<MovieListModel>

    suspend fun getMovieDetails(
        id: Int,
        api_key: String
    ): Response<MovieDataModel>
}

class MoviesRepo @Inject constructor(retrofit: Retrofit) : MoviesRepoInterface {

    private val api = retrofit.create(MoviesApi::class.java)

    override suspend fun getMovies(
        media_type: String,
        time_window: String,
        api_key: String
    ): Response<MovieListModel> =
        api.getMovies(media_type, time_window, api_key)

    override suspend fun getMovieDetails(id: Int, api_key: String): Response<MovieDataModel> =
        api.getMovieDetails(id, api_key)
}