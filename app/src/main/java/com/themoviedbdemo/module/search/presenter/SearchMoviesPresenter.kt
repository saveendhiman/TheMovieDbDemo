package com.themoviedbdemo.module.search.presenter

import com.themoviedbdemo.constants.ApiConstants
import com.themoviedbdemo.model.Movie
import com.themoviedbdemo.module.search.interactor.SearchMoviesData
import com.themoviedbdemo.module.search.target.SearchMoviesTarget
import com.themoviedbdemo.mvpbase.BasePresenter
import java.util.*
import javax.inject.Inject


/**
 * Created by Saveen on 21/01/18.
 */

class SearchMoviesPresenter @Inject constructor(private val upcomingTripData: SearchMoviesData) : BasePresenter<SearchMoviesTarget>() {


    fun getDiscoverMovies(query : String , page : Int) {

        upcomingTripData.executeDiscoverMovies(query, page)
                .doOnSubscribe { target?.showProgressDialog()}
                .doFinally { target?.closeProgressDialog()}
                .subscribe({

                    if (it.movies != null){

                        if (it.movies.size > 0) {
                            target!!.showList()
                        } else {
                            target!!.hideList()
                        }

                        target!!.showSearchMovesList(it.movies as ArrayList<Movie>, it.totalPages)

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

    fun getPopularMovies() {

        upcomingTripData.executePopularMovies()
                .doOnSubscribe { target?.showProgressDialog()}
                .doFinally { target?.closeProgressDialog()}
                .subscribe({

                    if (it.movies != null){

                        target!!.showPopularMovesList(it.movies as ArrayList<Movie>)
                    }

                }, {

                    target?.sendErrorToTarget(it)
                })
    }
}