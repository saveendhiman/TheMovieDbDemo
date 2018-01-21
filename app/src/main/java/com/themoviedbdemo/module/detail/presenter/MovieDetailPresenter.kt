package com.themoviedbdemo.module.detail.presenter

import com.themoviedbdemo.module.detail.interactor.MovieDetailData
import com.themoviedbdemo.module.detail.target.MovieDetailTarget
import com.themoviedbdemo.mvpbase.BasePresenter
import javax.inject.Inject


/**
 * Created by Saveen on 21/01/18.
 */

class MovieDetailPresenter @Inject constructor(private val upcomingTripData: MovieDetailData) : BasePresenter<MovieDetailTarget>() {


    fun getPopularMovies(movieID: Int) {

        upcomingTripData.executeMovieDetail(movieID)
                .doOnSubscribe { target?.showProgressDialog() }
                .doFinally { target?.closeProgressDialog() }
                .subscribe({

                    if (it != null) {
                        target!!.showMovieDetail(it)
                    }

                }, {

                    target?.sendErrorToTarget(it)
                })
    }

}