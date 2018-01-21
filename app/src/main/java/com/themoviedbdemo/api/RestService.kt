package com.themoviedbdemo.api

import com.themoviedbdemo.model.MovieListResponse
import com.themoviedbdemo.model.MovieResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Path

/**
 *  Created by Saveen on 21/01/18.
 */

interface RestService {

    @GET("/3/search/movie")
    fun discoverMovies(@Query("query") query: String, @Query("api_key") apiKey: String, @Query("page") page: Int): Observable<Response<MovieListResponse>>

    @GET("/3/movie/popular")
    fun fetchPopularMovies(@Query("api_key") apiKey: String ): Observable<Response<MovieListResponse>>

    @GET("/3/movie/popular")
    fun fetchPopularMoviesPages(@Query("api_key") apiKey: String, @Query("page") page: Int): Observable<Response<MovieListResponse>>

    @GET("/3/movie/top_rated")
    fun fetchHighestRatedMovies(@Query("api_key") apiKey: String ): Observable<Response<MovieListResponse>>

    @GET("/3/movie/top_rated")
    fun fetchHighestRatedMoviesPages(@Query("api_key") apiKey: String, @Query("page") page: Int): Observable<Response<MovieListResponse>>

    // Get movie details by id
    @GET("/3/movie/{id}")
    fun fetchMovie(@Path("id") movieId: Int, @Query("api_key") apiKey: String ): Observable<Response<MovieResponse>>

}
