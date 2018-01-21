package com.themoviedbdemo.module.detail.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.themoviedbdemo.R
import com.themoviedbdemo.base.BaseActivity
import com.themoviedbdemo.model.Movie
import com.themoviedbdemo.model.MovieResponse
import com.themoviedbdemo.module.detail.presenter.MovieDetailPresenter
import com.themoviedbdemo.module.detail.target.MovieDetailTarget
import com.themoviedbdemo.utils.AppUtils
import com.themoviedbdemo.utils.ImageUtility
import kotlinx.android.synthetic.main.activity_moviedetail.*
import org.jetbrains.annotations.NotNull
import javax.inject.Inject

/**
 * Created by Saveen on 21/01/18.
 */

class MovieDetailActivity : BaseActivity(), MovieDetailTarget {

    lateinit @Inject var presenter: MovieDetailPresenter
    lateinit @Inject var mImageUtility: ImageUtility
    lateinit @Inject var mAppUtils: AppUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
        presenter.takeTarget(this)

        setSupportActionBar(toolbarDetail as Toolbar?);

        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        (toolbarDetail as Toolbar?)?.setNavigationOnClickListener({ v -> onBackPressed() })

        val movieID  = intent.getIntExtra("movieID", -1)
        if (mAppUtils.isOnline(overviewTextView)) {
            presenter.getPopularMovies(movieID)
        }

    }

    companion object {

        @JvmStatic
        fun createIntent(@NotNull context: Context, movieID : Int): Intent {

            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra("movieID", movieID)
            return intent
        }

    }

    override val layout: Int
        get() = R.layout.activity_moviedetail

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropTarget()

    }

    override fun showMovieDetail(movieDetail: MovieResponse) {

        overviewTextView.setText(movieDetail.overview);
        durationTextView.setText(getDuration(movieDetail.movie));
        languageTextView.setText(movieDetail.originalLanguage);

        mImageUtility.loadImage(movieDetail.posterUrl, imageViewDetail!!, R.drawable.default_placeholder, false )

    }

    private fun getDuration(movie: Movie): String {
        val runtime = movie.runtime
        return if (runtime <= 0) "-" else resources.getQuantityString(R.plurals.duration, runtime, runtime)
    }

    private fun getGenres(movie: Movie): String {
        var genres = ""
        for (i in 0 until movie.movieGenres.size) {
            val genre = movie.movieGenres.get(i)
            genres += genre + ", "
        }
        genres = removeTrailingComma(genres)

        return if (genres.isEmpty()) "-" else genres
    }

    private fun removeTrailingComma(text: String): String {
        var text = text
        text = text.trim { it <= ' ' }
        if (text.endsWith(",")) {
            text = text.substring(0, text.length - 1)
        }
        return text
    }
}
