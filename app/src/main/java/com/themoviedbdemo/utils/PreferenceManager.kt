package com.themoviedbdemo.utils

import android.content.Context
import android.content.SharedPreferences
import com.themoviedbdemo.constants.PrefernceConstants

/**
 * Created by Saveen on 21/01/18.
 * Contains method to store and retrieve SharedPreferences data
 */
class PreferenceManager(context: Context) {

    init {
        Companion.context = context
    }

    //get shared pref
    private val preferences: SharedPreferences
        get() = context!!.getSharedPreferences(PrefernceConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)


    //set true when user is logegd in else false
    //returns true when user is logged in
    var isUserLoggedIn: Boolean
        get() = preferences.getBoolean(PrefernceConstants.IS_LOGIN, false)
        set(isLogin) {
            preferences.edit().putBoolean(PrefernceConstants.IS_LOGIN, isLogin).apply()
        }

    //set user password while login
    //returns user password while login
    var sessionToken: String
        get() = preferences.getString(PrefernceConstants.SESSION_TOKEN, "")
        set(sessionToken) {
            preferences.edit().putString(PrefernceConstants.SESSION_TOKEN, sessionToken).apply()
        }

    // returns user id
    //Set user id
    var userID: String
        get() = preferences.getString(PrefernceConstants.USER_ID, "")
        set(userid) {
            preferences.edit().putString(PrefernceConstants.USER_ID, userid).apply()
        }

    //set user password while login
    //returns user password while login
    var deviceToken: String
        get() = preferences.getString(PrefernceConstants.DEVICE_TOKEN, "")
        set(deviceToken) {
            preferences.edit().putString(PrefernceConstants.DEVICE_TOKEN, deviceToken).apply()
        }


//clear user shared preferences
    fun clearPrefrences() {
//        preferences.edit().clear().apply()
        preferences.edit().remove(PrefernceConstants.USER_ID).apply()
        preferences.edit().remove(PrefernceConstants.IS_LOGIN).apply()
        preferences.edit().remove(PrefernceConstants.SESSION_TOKEN).apply()
    }

    companion object {

        var context: Context? = null
            private set
    }

}