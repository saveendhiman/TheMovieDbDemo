package com.themoviedbdemo.module.home.presenter

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import com.themoviedbdemo.R
import com.themoviedbdemo.module.home.target.HomeTarget
import com.themoviedbdemo.module.home.view.HomeActivity
import com.themoviedbdemo.mvpbase.BasePresenter
import com.themoviedbdemo.utils.AppUtils
import com.themoviedbdemo.utils.FragmentUtils
import com.miguelcatalan.materialsearchview.MaterialSearchView
import javax.inject.Inject

/**
 * Created by Saveen on 21/01/18.
 */


class HomePresenter @Inject constructor( val mAppUtils : AppUtils, val mFragUtils: FragmentUtils) : BasePresenter<HomeTarget>() {

    private var doubleBackToExitPressedOnce = false

    //close app if drawer is not open else close drawer
    fun closeApp(drawerLayout: DrawerLayout, homeActivity: HomeActivity, searchView: MaterialSearchView) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (homeActivity.supportFragmentManager!!.backStackEntryCount == 0) {
                if (doubleBackToExitPressedOnce) {
                    homeActivity!!.finish()
                    return
                }
                if (searchView.isSearchOpen()) {
                    searchView.closeSearch()
                }
                this.doubleBackToExitPressedOnce = true
                mAppUtils.showToast(homeActivity.resources.getString(R.string.back_click_warning))

                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START)
                }

                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 3000)
            } else {
                homeActivity.supportFragmentManager!!.popBackStack()
            }
        }
    }

    //add a fragment
    fun addFragment(fragment: Fragment, bundle: Bundle?, mFragmentManager: FragmentManager) {
        mFragUtils.addFragment(R.id.frameContainer, mFragmentManager, fragment, bundle)
    }

    //replace fragment in frame
    fun replaceFragment(fragment: Fragment, bundle: Bundle?, mFragmentManager: FragmentManager) {
        mFragUtils.replaceFragment(R.id.frameContainer, mFragmentManager, fragment, bundle)
    }

    //remove all fragment from back Stack
    fun clearAllFragmentsFromBackStack(mFragmentManager: FragmentManager) {
        for (i in 0 until mFragmentManager.backStackEntryCount) {
            mFragmentManager.popBackStack()
        }
    }
}