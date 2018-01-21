package com.themoviedbdemo.module.top.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.themoviedbdemo.R
import com.themoviedbdemo.base.BaseFragment
import com.themoviedbdemo.model.Movie
import com.themoviedbdemo.module.detail.view.MovieDetailActivity
import com.themoviedbdemo.module.top.adapter.TopMoviesAdapter
import com.themoviedbdemo.module.top.presenter.TopMoviesPresenter
import com.themoviedbdemo.module.top.target.TopMoviesTarget
import com.themoviedbdemo.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_top.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Saveen on 21/01/18.
 */

class TopMoviesFragment : BaseFragment(), TopMoviesTarget, TopMoviesAdapter.TopMovieAdapterListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit @Inject var presenter: TopMoviesPresenter
    lateinit @Inject var mAppUtils: AppUtils

    lateinit var mLinearLayoutManager: LinearLayoutManager

    internal var scheduledRidesList = ArrayList<Movie>()
    private var page = 1
    private var hasMore = false
    lateinit var mAdapter : TopMoviesAdapter
    private var maxPagesCount = 0

    override val layoutId: Int
        get() = R.layout.fragment_top

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.takeTarget(this)

        mAdapter = TopMoviesAdapter(scheduledRidesList, this.activity!!, this)

        topSwipeRefreshLayout.setOnRefreshListener(this)

        mLinearLayoutManager = LinearLayoutManager(activity)
        topMoviesList.layoutManager = mLinearLayoutManager

        topMoviesList.addOnScrollListener(mRecyclerViewOnScrollListener)

        topMoviesList.adapter = mAdapter


        handleOnClick()
        if (mAppUtils.isOnline(topMoviesList)) {
            presenter.getRatedMovies(page)
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
                     if (mAppUtils.isOnline(topMoviesList)) {
                         presenter.getRatedMovies(page)
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
        if (mAppUtils.isOnline(topMoviesList)) {
            presenter.getRatedMovies(page)
        }

    }

    override fun hideList() {
        tvNoTopMoviesFound.visibility = View.VISIBLE
        topMoviesList.visibility = View.GONE

        stopRefreshing()
    }

    override fun showList() {
        tvNoTopMoviesFound.visibility = View.GONE
        topMoviesList.visibility = View.VISIBLE

        stopRefreshing()
    }

    /**
     * start refreshing
     */
    private fun startRefreshing() {
        topSwipeRefreshLayout.isEnabled = false
        topSwipeRefreshLayout.isRefreshing = true
    }

    /**
     * stop refreshing
     */
    private fun stopRefreshing() {
        topSwipeRefreshLayout.isEnabled = true
        if (topSwipeRefreshLayout.isRefreshing)
            topSwipeRefreshLayout.isRefreshing = false
    }

    override fun onMovieDetail(movieID: Int) {
        startActivity(MovieDetailActivity.createIntent(this.activity!! , movieID))
    }

}