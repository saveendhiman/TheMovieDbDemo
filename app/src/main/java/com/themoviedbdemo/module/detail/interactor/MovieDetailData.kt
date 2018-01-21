package com.themoviedbdemo.module.detail.interactor

import com.themoviedbdemo.api.RestService
import com.themoviedbdemo.constants.ApiConstants
import com.themoviedbdemo.model.MovieResponse
import com.themoviedbdemo.utils.ErrorResponseTransformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Saveen on 21/01/18.
 */

class MovieDetailData @Inject constructor(val restService: RestService) {

    fun executeMovieDetail(movieID : Int): Observable<MovieResponse> {

        return restService.fetchMovie( movieID, ApiConstants.tmdb_api_key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ErrorResponseTransformer<MovieResponse>())
    }

}