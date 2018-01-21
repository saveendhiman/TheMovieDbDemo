package com.themoviedbdemo.utils

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Created by Saveen on 21/01/18.
 * contains methods related to fragment transactions
 */
class FragmentUtils {

    // add fragment in a container
    fun addFragment(container: Int, manager: FragmentManager, fragment: Fragment, bundle: Bundle?) {
        if (bundle != null) {
            fragment.arguments = bundle
        }
        manager.beginTransaction().add(container, fragment).addToBackStack(null).commitAllowingStateLoss()
    }

    // replace fragment in a container
    fun replaceFragment(container: Int, manager: FragmentManager, fragment: Fragment, bundle: Bundle?) {
        if (bundle != null) {
            fragment.arguments = bundle
        }
        manager.beginTransaction().replace(container, fragment).commitAllowingStateLoss() // this is not so good @saveen
    }
}
