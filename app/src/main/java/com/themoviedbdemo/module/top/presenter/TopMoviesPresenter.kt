package com.themoviedbdemo.module.top.presenter

import com.themoviedbdemo.constants.ApiConstants
import com.themoviedbdemo.model.Movie
import com.themoviedbdemo.module.top.interactor.TopMoviesData
import com.themoviedbdemo.module.top.target.TopMoviesTarget
import com.themoviedbdemo.mvpbase.BasePresenter
import com.themoviedbdemo.utils.PreferenceManager
import java.util.*
import javax.inject.Inject


/**
 * Created by Saveen on 21/01/18.
 */

class TopMoviesPresenter @Inject constructor(private val upcomingTripData: TopMoviesData, val mPrefs: PreferenceManager) : BasePresenter<TopMoviesTarget>() {


    fun getRatedMovies(page : Int) {

        upcomingTripData.executeRatedMovies(page)
                .doOnSubscribe { target?.showProgressDialog()}
                .doFinally { target?.closeProgressDialog()}
                .subscribe({

                    if (it.movies != null){

                        if (it.movies.size > 0) {
                            target!!.showList()
                        } else {
                            target!!.hideList()
                        }

                        target!!.showPopularMovesList(it.movies as ArrayList<Movie>, it.totalPages)

                        if (it.movies.size < ApiConstants.PAGINATION_LIMIT) {
                            target!!.stopPagination()
                        } else {
                            target!!.hasMoreData()
                        }

                    }else{
                        target!!.stopPagination()
                        if (page == ApiConstants.PAGINATION_LIMIT) {
                            target!!.hideList()
                        }
                    }
                }, {

                    target?.sendErrorToTarget(it)
                })
    }

}