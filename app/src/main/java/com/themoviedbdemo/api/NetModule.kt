package com.themoviedbdemo.api

import com.themoviedbdemo.base.TheMovieDBApplication
import com.themoviedbdemo.constants.ApiConstants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Saveen on 21/01/18.
 * contains object related to network communication and API consumption
 *
 */
@Module
class NetModule(themoviedbApplication: TheMovieDBApplication) {

    val okHttpClient: OkHttpClient
        @Singleton
        @Provides
        get() {

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val builder = OkHttpClient.Builder()
            builder.interceptors().add(httpLoggingInterceptor)

            builder.readTimeout(60, TimeUnit.SECONDS)
            builder.connectTimeout(60, TimeUnit.SECONDS)

            return builder.build()
        }

    //get retrofit instance
    @Singleton
    @Provides
    fun getRestService(): RestService {

        val retrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        return retrofit.create(RestService::class.java)
    }

}