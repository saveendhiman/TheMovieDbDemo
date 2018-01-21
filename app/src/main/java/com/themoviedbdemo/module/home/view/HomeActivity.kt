package com.themoviedbdemo.module.home.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.annotation.RequiresApi
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.text.TextUtils
import android.view.*
import com.themoviedbdemo.R
import com.themoviedbdemo.base.BaseActivity
import com.themoviedbdemo.module.home.presenter.HomePresenter
import com.themoviedbdemo.module.home.target.HomeTarget
import com.themoviedbdemo.module.popular.view.PopularFragment
import com.themoviedbdemo.module.search.view.SearchMoviesFragment
import com.themoviedbdemo.module.top.view.TopMoviesFragment
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.annotations.NotNull
import javax.inject.Inject

/**
 * Created by Saveen on 21/01/18.
 */
class HomeActivity : BaseActivity(), HomeTarget, NavigationView.OnNavigationItemSelectedListener{

    lateinit @Inject var presenter: HomePresenter
    private lateinit var itemSearch : MenuItem

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
        presenter.takeTarget(this)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        presenter.addFragment(PopularFragment(), null, supportFragmentManager)

        searchView.setVoiceSearch(false)
        searchView.setCursorDrawable(R.drawable.color_cursor_white)
        searchView.setSuggestions(resources.getStringArray(R.array.query_suggestions))
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                val broadcastIntent = Intent("SearchMoviesReceived")
                broadcastIntent.putExtra("searchString", query)
                sendBroadcast(broadcastIntent)

                searchView.dismissSuggestions()

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                searchView.dismissSuggestions()

                return false
            }
        })

        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {

            }

            override fun onSearchViewClosed() {
                searchView.dismissSuggestions()
            }
        })
    }

    companion object {

        @JvmStatic
        fun createIntent(@NotNull context: Context): Intent {

            val intent = Intent(context, HomeActivity::class.java)
            return intent
        }

    }

    override val layout: Int
        get() = R.layout.activity_home

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropTarget()
    }

    override fun onBackPressed() {

        presenter.closeApp(drawer_layout, this@HomeActivity, searchView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        itemSearch = menu.findItem(R.id.action_search)
        searchView.setMenuItem(itemSearch)
        itemSearch.isVisible = false

        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_popular -> {

                itemSearch.isVisible = false
                presenter.replaceFragment(PopularFragment(), null,supportFragmentManager)
            }
            R.id.nav_search -> {
                itemSearch.isVisible = true
                presenter.replaceFragment(SearchMoviesFragment(), null,supportFragmentManager)
            }
            R.id.nav_top -> {

                itemSearch.isVisible = false
                presenter.replaceFragment(TopMoviesFragment(), null,supportFragmentManager)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return false
        // If you return true then selection of navigation will show.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            val matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null && matches.size > 0) {
                val searchWrd = matches[0]
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false)
                }
            }

            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}