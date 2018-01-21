package com.themoviedbdemo.mvpbase

import com.themoviedbdemo.utils.TheMovieDBException


/**
 * Created by Saveen on 21/01/18.
 */
interface BaseTarget {

    fun showErrorMessage(exception: TheMovieDBException) {}

    fun showProgressDialog(message: String? = null) {}
    fun closeProgressDialog() {}

    fun sendErrorToTarget(it: Throwable) {
        if (it is TheMovieDBException)
            showErrorMessage(exception = it)
        else
            showErrorMessage(TheMovieDBException(-1)) // unknown
    }
}