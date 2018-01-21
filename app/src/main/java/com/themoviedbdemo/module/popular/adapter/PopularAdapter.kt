package com.themoviedbdemo.module.popular.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.themoviedbdemo.R
import com.themoviedbdemo.base.TheMovieDBApplication
import com.themoviedbdemo.model.Movie
import com.themoviedbdemo.utils.ImageUtility
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Saveen on 21/01/18.
 */

class PopularAdapter(val list: ArrayList<Movie>, val context: Context, private val popularListener: PopularAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit @Inject var mImageUtility: ImageUtility

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        val arrayList = list[position]
        holder as PopularViewHolder

        mImageUtility.loadImage(arrayList.posterUrl, holder.imageView!!, R.drawable.default_placeholder, false )
        holder.popularityTextView!!.text = getPopularityString(arrayList.popularity)
        holder.titleTextView!!.text = arrayList.title
        holder.overviewTextView!!.text = arrayList.overview

        holder.parent!!.setOnClickListener { popularListener.onMovieDetail(arrayList.id) }
        holder.tvReadMore!!.setOnClickListener { popularListener.onMovieDetail(arrayList.id) }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_popularmovies, parent, false)
        return PopularViewHolder(view)
    }


    class PopularViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        var imageView: ImageView? = null
        var popularityTextView: TextView? = null
        var titleTextView: TextView? = null
        var overviewTextView: TextView? = null
        var tvReadMore: TextView? = null
        var parent: RelativeLayout? = null

        init {
            imageView = item.findViewById(R.id.imageView)
            popularityTextView = item.findViewById(R.id.popularityTextView)
            titleTextView = item.findViewById(R.id.titleTextView)
            overviewTextView = item.findViewById(R.id.overviewTextView)
            tvReadMore = item.findViewById(R.id.tvReadMore)
            parent = item.findViewById(R.id.rlsearchLocationRoot)
        }
    }

    init {
        (context.applicationContext as TheMovieDBApplication).appComponent!!.inject(this)
    }

    interface PopularAdapterListener {

        fun onMovieDetail(movieID: Int)
    }

    private fun getPopularityString(popularity: Float): String {
        val decimalFormat = java.text.DecimalFormat("#.##")
        return decimalFormat.format(popularity.toDouble())
    }

}
