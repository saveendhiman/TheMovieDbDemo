package com.themoviedbdemo.module.search.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.themoviedbdemo.R
import com.themoviedbdemo.base.BaseFragment
import com.themoviedbdemo.model.Movie
import com.themoviedbdemo.module.detail.view.MovieDetailActivity
import com.themoviedbdemo.module.search.adapter.SearchMoviesAdapter
import com.themoviedbdemo.module.search.presenter.SearchMoviesPresenter
import com.themoviedbdemo.module.search.target.SearchMoviesTarget
import com.themoviedbdemo.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Saveen on 21/01/18.
 */

class SearchMoviesFragment : BaseFragment(), SearchMoviesTarget, SearchMoviesAdapter.SearchAdapterListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit @Inject var presenter: SearchMoviesPresenter
    lateinit @Inject var mAppUtils: AppUtils
    lateinit var mLinearLayoutManager: LinearLayoutManager

    internal var scheduledRidesList = ArrayList<Movie>()
    private var page = 1
    private var hasMore = false
    lateinit var mAdapter : SearchMoviesAdapter
    private var maxPagesCount = 0
    private var searchString : String = ""

    private val searchMoviesReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                searchString = intent!!.getStringExtra("searchString")
                if (mAppUtils.isOnline(searchMoviesList)) {
                    if (scheduledRidesList.size > 0){
                        scheduledRidesList.clear()
                    }
                    presenter.getDiscoverMovies(searchString, page)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.takeTarget(this)
        activity!!.registerReceiver(searchMoviesReceiver, IntentFilter("SearchMoviesReceived"))

        mAdapter = SearchMoviesAdapter(scheduledRidesList, this.activity!!, this)

        searchSwipeRefreshLayout.setOnRefreshListener(this)

        mLinearLayoutManager = LinearLayoutManager(activity)
        searchMoviesList.layoutManager = mLinearLayoutManager

        searchMoviesList.addOnScrollListener(mRecyclerViewOnScrollListener)

        searchMoviesList.adapter = mAdapter
        if (mAppUtils.isOnline(searchMoviesList)) {
            presenter.getPopularMovies()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropTarget()
        activity!!.unregisterReceiver(searchMoviesReceiver)
    }

    override fun showSearchMovesList(arrayList : ArrayList<Movie>, maxPagesCount : Int) {

        if (arrayList.size == 0) {
            hideList()
            return
        }
        this.maxPagesCount = maxPagesCount
        scheduledRidesList.addAll(arrayList)
        mAdapter.notifyDataSetChanged()

        stopRefreshing()

    }

    override fun showPopularMovesList(arrayList: ArrayList<Movie>) {

        if (arrayList.size == 0) {
            hideList()
            return
        }
        scheduledRidesList.addAll(arrayList)
        mAdapter.notifyDataSetChanged()
    }

    //on scroll listener for recycler view. Pagination logic works here
    private val mRecyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
             if (hasMore && mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == mAdapter.itemCount - 1 ) {
                 page += 1

                 if (page < maxPagesCount){
                     if (mAppUtils.isOnline(searchMoviesList)) {
                         presenter.getDiscoverMovies(searchString,page)
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
        if (mAppUtils.isOnline(searchMoviesList)) {
            presenter.getDiscoverMovies(searchString, page)
        }
    }

    override fun hideList() {
        tvNoSearchMoviesFound.visibility = View.VISIBLE
        searchMoviesList.visibility = View.GONE

        stopRefreshing()
    }

    override fun showList() {
        tvNoSearchMoviesFound.visibility = View.GONE
        searchMoviesList.visibility = View.VISIBLE

        stopRefreshing()
    }

    /**
     * start refreshing
     */
    private fun startRefreshing() {
        searchSwipeRefreshLayout.isEnabled = false
        searchSwipeRefreshLayout.isRefreshing = true
    }

    /**
     * stop refreshing
     */
    private fun stopRefreshing() {
        searchSwipeRefreshLayout.isEnabled = true
        if (searchSwipeRefreshLayout.isRefreshing)
            searchSwipeRefreshLayout.isRefreshing = false
    }

    override fun onMovieDetail(movieID: Int) {
        startActivity(MovieDetailActivity.createIntent(this.activity!! , movieID))
    }

}