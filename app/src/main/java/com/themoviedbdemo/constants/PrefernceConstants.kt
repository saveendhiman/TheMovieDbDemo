package com.themoviedbdemo.constants

/**
 * Created by Saveen on 21/01/18.
 * Contains all the keys used in SharedPrefernce of application
 */
interface PrefernceConstants {
    companion object {

        val PREFERENCE_NAME = "THEMOVIEDB_PREFERENCES"
        val IS_LOGIN = "isLogin"
        val SESSION_TOKEN = "session_token"
        val USER_ID = "user_id"
        val DEVICE_TOKEN = "device_token"

    }
}