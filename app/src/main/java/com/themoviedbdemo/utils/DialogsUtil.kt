package com.themoviedbdemo.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.support.annotation.LayoutRes
import android.view.ViewGroup
import android.view.Window
import com.themoviedbdemo.R

import com.themoviedbdemo.interfaces.OnDialogButtonClickListener

/**
 * Created by Saveen on 21/01/18.
 * contains Dialogs to be used in the application
 */
class DialogsUtil {


    /**
     * Return an alert dialog

     * @param message  message for the alert dialog
     * *
     * @param listener listener to trigger selection methods
     */
    fun openAlertDialog(context: Context, message: String, positiveBtnText: String, negativeBtnText: String,
                        listener: OnDialogButtonClickListener) {

        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton(positiveBtnText) { dialog, which ->
            dialog.dismiss()
            listener.onPositiveButtonClicked()

        }

        builder.setNegativeButton(negativeBtnText) { dialog, which ->
            dialog.dismiss()
            listener.onNegativeButtonClicked()

        }
        builder.setTitle(context.resources.getString(R.string.payment))
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setCancelable(false)
        builder.create().show()
    }

    /**
     * return a dialog object

     * @return normal dialog
     */
    fun getDialog(context: Context, @LayoutRes layoutId: Int): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(layoutId)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

    /**
     * return a dialog object

     * @return normal dialog
     */
    fun getDialogNonDismissible(context: Context, @LayoutRes layoutId: Int): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(layoutId)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

}