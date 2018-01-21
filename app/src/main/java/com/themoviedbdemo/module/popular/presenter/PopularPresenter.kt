package com.themoviedbdemo.module.popular.presenter

import com.themoviedbdemo.constants.ApiConstants
import com.themoviedbdemo.model.Movie
import com.themoviedbdemo.module.popular.interactor.PopularData
import com.themoviedbdemo.module.popular.target.PopularTarget
import com.themoviedbdemo.mvpbase.BasePresenter
import java.util.*
import javax.inject.Inject

/**
 * Created by Saveen on 21/01/18.
 */

class PopularPresenter @Inject constructor(private val upcomingTripData: PopularData) : BasePresenter<PopularTarget>() {


    fun getPopularMovies(page : Int) {

        upcomingTripData.executePopularMovies(page)
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