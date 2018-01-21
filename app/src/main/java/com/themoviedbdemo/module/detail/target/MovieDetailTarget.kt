package com.themoviedbdemo.module.detail.target

import com.themoviedbdemo.model.MovieResponse
import com.themoviedbdemo.mvpbase.BaseTarget

/**
 * Created by Saveen on 21/01/18.
 */

interface MovieDetailTarget : BaseTarget {

    fun showMovieDetail(movieDetail : MovieResponse)

}