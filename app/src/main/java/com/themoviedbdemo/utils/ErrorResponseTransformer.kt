package com.themoviedbdemo.utils

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.UnknownHostException


class ErrorResponseTransformer<T> : ObservableTransformer<Response<T>, T> {

    override fun apply(upstream: Observable<Response<T>>): ObservableSource<T> {
        return upstream
                .onErrorReturn {
                    if (it is UnknownHostException) {
                        Response.error(5000, ResponseBody.create(MediaType.parse("application/json"), Gson().toJson(ApiError(29, "Phew Phew! \n it seems like you are not connect to internet world. Please connect and try again."))))
                    } else {
                        Response.error(5001, ResponseBody.create(MediaType.parse("application/json"), Gson().toJson(ApiError(30, it.localizedMessage))))
                    }

                }
                .flatMap {
                    if (it.isSuccessful) {
                        Observable.just(it.body())
                    } else {
                        val error = Gson().fromJson(it.errorBody()!!.string(), ApiError::class.java)
                        Observable.error(TheMovieDBException(error.statusCode, error.message))
                    }
                }

    }


}