package com.themoviedbdemo.base

import android.app.AlertDialog
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.themoviedbdemo.R
import com.themoviedbdemo.mvpbase.BaseTarget
import com.themoviedbdemo.utils.TheMovieDBException
import dmax.dialog.SpotsDialog

/**
 * Created by Saveen on 21/01/18.
 * To be extended by all fragments
 */
abstract class BaseFragment : Fragment(), BaseTarget {

    internal var progressDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(layoutId, container, false)

        return view
    }


    @get:LayoutRes
    abstract val layoutId: Int

    val component: AppComponent
        get() = (activity!!.application as TheMovieDBApplication).appComponent

    override fun showProgressDialog(message: String?) {
        if (activity == null)
            return

        if (progressDialog == null)
            progressDialog = SpotsDialog(activity, message ?: "Please wait")
        progressDialog!!.setCancelable(false)

        progressDialog!!.show()
    }

    override fun closeProgressDialog() {
        if (progressDialog != null)
            progressDialog!!.dismiss()
    }

    fun showGeneralAlert(positiveButtonText: String?, negativeButtonText: String?, message: String) {
        val builder = AlertDialog.Builder(activity)
        if (positiveButtonText != null)
            builder.setPositiveButton(positiveButtonText) { dialog, which -> dialog.dismiss() }
        if (negativeButtonText != null)
            builder.setNegativeButton(negativeButtonText) { dialog, which -> dialog.dismiss() }
        builder.setTitle(resources.getString(R.string.app_name))
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.create().show()
    }

    override fun showErrorMessage(exception: TheMovieDBException) {
        showGeneralAlert("OK", null, exception.getLocalisedString())
    }

}
