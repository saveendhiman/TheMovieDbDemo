package com.themoviedbdemo.base

import com.themoviedbdemo.api.NetModule
import com.themoviedbdemo.module.detail.view.MovieDetailActivity
import com.themoviedbdemo.module.home.view.HomeActivity
import com.themoviedbdemo.module.popular.adapter.PopularAdapter
import com.themoviedbdemo.module.popular.view.PopularFragment
import com.themoviedbdemo.module.search.adapter.SearchMoviesAdapter
import com.themoviedbdemo.module.search.view.SearchMoviesFragment
import com.themoviedbdemo.module.top.adapter.TopMoviesAdapter
import com.themoviedbdemo.module.top.view.TopMoviesFragment
import com.themoviedbdemo.utils.UtilsModule
import javax.inject.Singleton
import dagger.Component

/**
 * Created by Saveen on 21/01/18.
 * Injections for Application class
 */

@Singleton
@Component(modules = arrayOf(UtilsModule::class, NetModule::class))
interface AppComponent {

    fun inject(baseApp: TheMovieDBApplication)

    fun inject(homeActivity : HomeActivity)

    fun inject(popularFragment : PopularFragment)

    fun inject(searchMoviesFragment : SearchMoviesFragment)

    fun inject(topMoviesFragment : TopMoviesFragment)

    fun inject(popularAdapter : PopularAdapter)

    fun inject(searchMoviesAdapter : SearchMoviesAdapter)

    fun inject(topMoviesAdapter : TopMoviesAdapter)

    fun inject(movieDetailActivity : MovieDetailActivity)
}