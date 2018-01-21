package com.themoviedbdemo.utils

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.TextView
import android.widget.Toast
import com.themoviedbdemo.R
import com.themoviedbdemo.base.TheMovieDBApplication
import com.themoviedbdemo.constants.ApiConstants
import org.json.JSONObject

import java.io.File
import java.net.ConnectException
import java.net.UnknownHostException

import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Saveen on 21/01/18.
 *
 * Contains commonly used methods in an Android App
 */
class AppUtils(private val mContext: TheMovieDBApplication) {

    /**
     * Description : Check if user is online or not

     * @return true if online else false
     */
    fun isOnline(v: View): Boolean {
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnectedOrConnecting) {
            return true
        }
        showSnackBar(v, mContext.getString(R.string.toast_network_not_available))
        return false
    }


    /**
     * Description : Hide the soft keyboard

     * @param view : Pass the current view
     */
    fun hideSoftKeyboard(view: View) {
        val inputMethodManager = mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Description : Hide the soft keyboard

     * @param view : Pass the current view
     */
    fun showSoftKeyboardForce() {

        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hidewSoftKeyboardForce() {

        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }


    /**
     * Show snackbar

     * @param view view clicked
     * *
     * @param text text to be displayed on snackbar
     */
    fun showSnackBar(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Show snackbar

     * @param text text to be displayed on Toast
     */
    fun showToast(text: String) {
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show()
    }


    /**
     * show related error message to user on api failure
     */
    fun showErrorMessage(view: View, t: Throwable) {
        showSnackBar(view, getErrorMessage(t))
    }

    //return error message from webservice error code
    private fun getErrorMessage(throwable: Throwable): String {
        val errorMessage: String
        if (throwable is UnknownHostException || throwable is ConnectException) {
            errorMessage = mContext.resources.getString(R.string.warning_network_error)
        } else {
            errorMessage = "Unfortunately an error has occurred!"
        }
        return errorMessage
    }


    /**
     * redirect user to your application settings in device
     */
    fun redirectToAppSettings(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", mContext.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    /**
     * check if user has enabled Gps of device

     * @return true or false depending upon device Gps status
     */
    val isGpsEnabled: Boolean
        get() {
            val manager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

    /**
     * Redirect user to enable GPS
     */
    fun goToGpsSettings() {
        val callGPSSettingIntent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        mContext.startActivity(callGPSSettingIntent)
    }


    @SuppressLint("MissingPermission")
    fun callByIntent(number: String, activity: Activity) {

        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number))
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        activity.startActivity(intent)
    }


    /**
     * Get error message from api's if status code is 400, 401

     * @return error message from response
     */
    fun getErrorMessage(errormsg: String): String {

        var errormessage = ""
        try {
            val jObjError = JSONObject(errormsg)
            errormessage = jObjError.getString(ApiConstants.geterrorMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return errormessage
    }

    //return multipart string
    fun getRequestString(text: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), text)
    }

    /**
     * get request body image
     */
    fun getRequestBodyImage(path: String): RequestBody {
        val file = File(path)
        return RequestBody.create(MediaType.parse("image/png"), file)
    }

    /**
     * Text to html form

     * @param html
     * *
     * @return
     */

    fun fromHtml(html: String): Spanned {
        val result: Spanned
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            result = Html.fromHtml(html)
        }
        return result
    }


    /**
     * Capitalized the first letter in any string

     * @param passString
     * *
     * @return
     */
    fun capitalizedFirstLetter(passString: String): String {

        val sb = StringBuilder(passString)
        sb.setCharAt(0, Character.toUpperCase(sb[0]))
        return sb.toString()
    }

    /**
     * Open play store
     * @param context
     * *
     * @param appPackageName
     */
    fun openAppInGooglePlay(context: Context, appPackageName: String) {
        //        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)))
        } catch (e: android.content.ActivityNotFoundException) { // if there is no Google Play on device
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)))
        }

    }

    @SuppressLint("MissingPermission")
    fun getUniqueId(context: Context): String? {

        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val imei = telephonyManager.deviceId
            if (imei != null && !imei.isEmpty()) {
                return imei
            }
            val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if (androidId != null && !androidId.isEmpty()) {
                return androidId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null

    }

    fun getDimensions(context: Context): IntArray {
        val width = context.resources.displayMetrics.widthPixels
        val height = context.resources.displayMetrics.heightPixels
        return intArrayOf(width, height)
    }

    fun getDeviceDensity(context: Context): String {
        var deviceDensity = ""
        when (context.resources.displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> deviceDensity = 0.75.toString() + " ldpi"
            DisplayMetrics.DENSITY_MEDIUM -> deviceDensity = 1.0.toString() + " mdpi"
            DisplayMetrics.DENSITY_HIGH -> deviceDensity = 1.5.toString() + " hdpi"
            DisplayMetrics.DENSITY_XHIGH -> deviceDensity = 2.0.toString() + " xhdpi"
            DisplayMetrics.DENSITY_XXHIGH -> deviceDensity = 3.0.toString() + " xxhdpi"
            DisplayMetrics.DENSITY_XXXHIGH -> deviceDensity = 4.0.toString() + " xxxhdpi"
            else -> deviceDensity = "Not found"
        }
        return deviceDensity
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    /**
     * To make the EditText scrollable inside the ScrollView. This methods allow editText to intercept touch event in scrollView.
     *
     * @param editText
     */
    fun makeEditTextScrollableInsideScrollView(editText: EditText) {
        editText.setOnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    fun showShortToast(context: Context, message: String, bgColor: Int) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        val view = toast.view
        view.setBackgroundColor(bgColor)
        val text = view.findViewById<View>(android.R.id.message) as TextView
        text.setTextColor(Color.WHITE)
        val leftPadding = context.resources.getDimensionPixelSize(R.dimen.margin_2x)
        val rightPadding = context.resources.getDimensionPixelSize(R.dimen.margin_2x)
        text.setPadding(leftPadding, 0, rightPadding, 0)
        toast.show()
    }

    /**
     * Make HorizontalScrollView scrolling to focus on the View passed in the param
     *
     * @param scrollView
     * @param view
     */
    fun scrollToFocusOnView(scrollView: HorizontalScrollView, view: View) {
        scrollView.post {
            val vLeft = view.left
            val vRight = view.right
            val sWidth = scrollView.width
            scrollView.smoothScrollTo((vLeft + vRight - sWidth) / 2, 0)
        }
    }


    /**
     * This compress the image using CompressFormat.JPEG and write back to the same file
     *
     * @param imageUrl
     * @param compressionRatio 1 == originalImage, 2 = 50% compression, 4=25% compress
     */
    fun compressImageFile(imageUrl: String, compressionRatio: Int, maxWidth: Int, maxHeight: Int) {
        val file = File(imageUrl)
        try {
            Log.i("AppUtil", "Size before compression: " + file.length())
            val bitmap = getBitmapFromFile(imageUrl, maxWidth, maxHeight)
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatio, FileOutputStream(file))
            Log.i("AppUtil", "Size after  compression: " + file.length())
        } catch (t: Throwable) {
            Log.e("ERROR", "Error compressing file." + t.toString())
            t.printStackTrace()
        }

    }

    fun getBitmapFromFile(imageFilePath: String, width: Int, height: Int): Bitmap {
        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / width, photoH / height)

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        return BitmapFactory.decodeFile(imageFilePath, bmOptions)
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    @Throws(IOException::class)
    fun copyFile(sourceFile: File, destFile: File) {
        if (!sourceFile.exists()) {
            return
        }

        var source: FileChannel? = null
        var destination: FileChannel? = null
        source = FileInputStream(sourceFile).channel
        destination = FileOutputStream(destFile).channel
        if (destination != null && source != null) destination.transferFrom(source, 0, source.size())
        if (source != null) source.close()
        if (destination != null) destination.close()
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {

            context.resources.configuration.locale
        }
    }

}