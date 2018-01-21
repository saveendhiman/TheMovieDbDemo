package com.themoviedbdemo.module.top.target

import com.themoviedbdemo.model.Movie
import com.themoviedbdemo.mvpbase.BaseTarget
import java.util.ArrayList

/**
 * Created by Saveen on 21/01/18.
 */

interface TopMoviesTarget : BaseTarget {

    fun showPopularMovesList(scheduledRidesList : ArrayList<Movie>, totalPages : Int)

    fun hideList()

    fun showList()

    fun stopPagination()

    fun hasMoreData()
}