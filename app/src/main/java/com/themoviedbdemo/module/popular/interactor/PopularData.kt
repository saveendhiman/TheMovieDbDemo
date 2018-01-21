package com.themoviedbdemo.module.popular.interactor

import com.themoviedbdemo.api.RestService
import com.themoviedbdemo.constants.ApiConstants
import com.themoviedbdemo.model.MovieListResponse
import com.themoviedbdemo.utils.AppUtils
import com.themoviedbdemo.utils.ErrorResponseTransformer
import com.themoviedbdemo.utils.PreferenceManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Saveen on 21/01/18.
 */

class PopularData @Inject constructor(val restService: RestService, val mAppUtils: AppUtils, val mPrefs: PreferenceManager) {


    fun executePopularMovies(page : Int): Observable<MovieListResponse> {

        return restService.fetchPopularMoviesPages(ApiConstants.tmdb_api_key, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ErrorResponseTransformer<MovieListResponse>())
    }

}