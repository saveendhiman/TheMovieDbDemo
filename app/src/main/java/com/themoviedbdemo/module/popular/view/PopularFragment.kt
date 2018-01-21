package com.themoviedbdemo.module.popular.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.themoviedbdemo.R
import com.themoviedbdemo.base.BaseFragment
import com.themoviedbdemo.model.Movie
import com.themoviedbdemo.module.detail.view.MovieDetailActivity
import com.themoviedbdemo.module.popular.adapter.PopularAdapter
import com.themoviedbdemo.module.popular.presenter.PopularPresenter
import com.themoviedbdemo.module.popular.target.PopularTarget
import com.themoviedbdemo.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_popular.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Saveen on 21/01/18.
 */

class PopularFragment : BaseFragment(), PopularTarget, PopularAdapter.PopularAdapterListener, SwipeRefreshLayout.OnRefreshListener {


    lateinit @Inject var presenter: PopularPresenter
    lateinit @Inject var mAppUtils: AppUtils

    lateinit var mLinearLayoutManager: LinearLayoutManager

    internal var scheduledRidesList = ArrayList<Movie>()
    private var page = 1
    private var hasMore = false
    lateinit var mAdapter : PopularAdapter
    private var maxPagesCount = 0

    override val layoutId: Int
        get() = R.layout.fragment_popular

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.takeTarget(this)

        mAdapter = PopularAdapter(scheduledRidesList, this.activity!!, this)

        popularSwipeRefreshLayout.setOnRefreshListener(this)

        mLinearLayoutManager = LinearLayoutManager(activity)
        popularMoviesList.layoutManager = mLinearLayoutManager

        popularMoviesList.addOnScrollListener(mRecyclerViewOnScrollListener)

        popularMoviesList.adapter = mAdapter

        handleOnClick()
        if (mAppUtils.isOnline(popularMoviesList)) {
            presenter.getPopularMovies(page)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropTarget()

    }

    fun handleOnClick() {

    }

    override fun showPopularMovesList(arrayList : ArrayList<Movie>, maxPagesCount : Int) {

        if (arrayList.size == 0) {
            hideList()
            return
        }
        this.maxPagesCount = maxPagesCount
        scheduledRidesList.addAll(arrayList)
        mAdapter.notifyDataSetChanged()

        stopRefreshing()

    }


    //on scroll listener for recycler view. Pagination logic works here
    private val mRecyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
             if (hasMore && mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == mAdapter.itemCount - 1 ) {
                 page += 1

                 if (page < maxPagesCount){
                     if (mAppUtils.isOnline(popularMoviesList)) {
                         presenter.getPopularMovies( page)
                     }
                     hasMore = false
                 }else{

                 }

                }
        }
    }


    override fun stopPagination() {
        hasMore = false
    }

    override fun hasMoreData() {
        hasMore = true
    }


    override fun onRefresh() {

        startRefreshing()

        scheduledRidesList.clear()
        mAdapter.notifyDataSetChanged()
        hasMore = false
        page = 1
        if (mAppUtils.isOnline(popularMoviesList)) {
            presenter.getPopularMovies(page)
        }
    }

    override fun hideList() {
        tvNoPopularMoviesFound.visibility = View.VISIBLE
        popularMoviesList.visibility = View.GONE

        stopRefreshing()
    }

    override fun showList() {
        tvNoPopularMoviesFound.visibility = View.GONE
        popularMoviesList.visibility = View.VISIBLE

        stopRefreshing()
    }

    /**
     * start refreshing
     */
    private fun startRefreshing() {
        popularSwipeRefreshLayout.isEnabled = false
        popularSwipeRefreshLayout.isRefreshing = true
    }

    /**
     * stop refreshing
     */
    private fun stopRefreshing() {
        popularSwipeRefreshLayout.isEnabled = true
        if (popularSwipeRefreshLayout.isRefreshing)
            popularSwipeRefreshLayout.isRefreshing = false
    }

    override fun onMovieDetail(movieID: Int) {

        startActivity(MovieDetailActivity.createIntent(this.activity!! , movieID))
    }
}