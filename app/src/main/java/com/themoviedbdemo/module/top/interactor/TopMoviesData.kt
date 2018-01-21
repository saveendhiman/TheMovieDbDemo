package com.themoviedbdemo.module.top.interactor

import com.themoviedbdemo.api.RestService
import com.themoviedbdemo.constants.ApiConstants
import com.themoviedbdemo.model.MovieListResponse
import com.themoviedbdemo.utils.AppUtils
import com.themoviedbdemo.utils.ErrorResponseTransformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Saveen on 21/01/18.
 */

class TopMoviesData @Inject constructor(val restService: RestService, val mAppUtils: AppUtils) {

    fun executeRatedMovies(page : Int): Observable<MovieListResponse> {

        return restService.fetchHighestRatedMoviesPages(ApiConstants.tmdb_api_key, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ErrorResponseTransformer<MovieListResponse>())
    }

}